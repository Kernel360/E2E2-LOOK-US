package org.example.image;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.redis.service.ImageRedisService;
import org.example.post.repository.custom.UpdateScoreType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * NOTE: 비동기 호출에 대한 해결 방법은 총 2가지입니다.
 *        -
 *       (1) 방법: updateZSetColorScore 함수 호출 전에 image분석 정보가 있는지 체크하고, 있을 때만 score를 update 합니다.
 *           한계: 이미지 분석이 아직 진행 중일 때 발생한 likes, scrap, view 업데이트는 score가 업데이트 되지 않습니다.
 *        -
 *       (2) 방법: job queue의 모든 작업 중에서 imageLocationId와 같은 이미지를 분석하려는 작업이 있는지 검사합니다.
 *                분석하려는 작업이 하나라도 큐에 있다면, 그것은 작업이 끝난 상태일 것입니다. (근데 아닐수도...?):
 *           한계: 설정이 쉽지 않다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncImageAnalyzer {

	/**
	 * 이미지 분석이 끝나지 않은 상태 에서 게시물 조회 발생시, 잠시 대기 후 비동기 요청을 재시도 합니다.
	 * 분석이 종료되기 까지 평균 1.5초 소요됬던 점을 바탕으로 1000ms로 설정했습니다.
	 */
	private static final int UPDATE_SCORE_PENDING_WAIT_MS = 1000;

	private final ImageAnalyzeManager imageAnalyzeManager;
	private final ImageRedisService imageRedisService;

	// ------------------------ (1) Multi Threaded ------------------------------------------
	// Must Test concurrency problem, if executor is not single threaded
	//       - https://stackoverflow.com/questions/24981551/concurrency-on-treeset-using-last-method
	// private final Set<Long> memoForOnProcessingTasks = Collections.synchronizedSortedSet(new TreeSet<>());
	// Executor scheduledThreadPoolExecutor = Executors.newScheduledThreadPool(10);

	// ------------------------ (2) Single Threaded ------------------------------------------
	// No synchronization required when accessing objects that are not thread-safe
	//       - https://www.baeldung.com/java-completablefuture-threadpool
	private final Set<Long> memoForOnProcessingTasks = new HashSet<>();
	Executor singleThreadExecutor = Executors.newSingleThreadExecutor();

	public void run(Long imageLocationId) {
		log.info("\n\n[AsyncImageAnalyzer] : ------- (0) Running... {}\n", imageLocationId);
		CompletableFuture
			.runAsync(() -> {
				try {
					memoForOnProcessingTasks.add(imageLocationId);
					imageAnalyzeManager.analyze(imageLocationId);
					log.info(
						"\n\n[AsyncImageAnalyzer] : ------- (1) analyze image done - id image {}\n",
						imageLocationId
					);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}, singleThreadExecutor)
			.thenRunAsync(() -> {
				try {
					var colorList = imageRedisService.saveNewColor(imageLocationId);
					log.info(
						"\n\n[AsyncImageAnalyzer] : ------- (2) save new color list done - image {}, colorList {}\n",
						imageLocationId, colorList
					);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}, singleThreadExecutor)
			.whenComplete((__, exception) -> {
				memoForOnProcessingTasks.remove(imageLocationId);
				log.info(
					"\n\n[AsyncImageAnalyzer] : ------- (3) async pipeline completed - image {}\n",
					imageLocationId
				);
				if (exception != null) {
					throw new RuntimeException(exception); // TODO: throw custom async exception
				}
			});
	}

	public void updateScore(
		Long imageLocationId,
		UpdateScoreType updateScoreType
	) throws JsonProcessingException {

		if ( isScoreNotInitialized(imageLocationId) ) {
			// add to task queue
			CompletableFuture.runAsync(() -> {
				try {
					// 1. do pending
					log.info(
						"\n\n[AsyncImageAnalyzer] : Pending Score Update ... {} - score type {}\n",
						imageLocationId, updateScoreType
					);
					Thread.sleep(UPDATE_SCORE_PENDING_WAIT_MS);
					// 2. retry after sleep
					log.info(
						"\n\n[AsyncImageAnalyzer] : Retrying Score Update {} - score type {}\n",
						imageLocationId, updateScoreType
					);
					this.updateScore(imageLocationId, updateScoreType);

				} catch (JsonProcessingException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			}, singleThreadExecutor).orTimeout(
				// 혹시 모를 재귀 호출로 인한 문제를 방지하고자, Pending 후 500ms 이후에 해당 Job을 끝냅니다.
				// Will throw a TimeoutException in case of a timeout. (# JUST FOR SAFETY)
				//  - Ref: https://www.baeldung.com/java-completablefuture-timeout
				UPDATE_SCORE_PENDING_WAIT_MS + 5000, // max 5 second
				TimeUnit.MILLISECONDS
			).whenComplete((__, exception) -> {
				if (exception != null) {
					throw new RuntimeException(exception);
				}
				log.info(
					"\n\n[AsyncImageAnalyzer] : Update Score Async Pending Finished - Image {}\n",
					imageLocationId
				);
			});

			return;
		}

		this.imageRedisService.updateZSetColorScore(imageLocationId, updateScoreType);

		log.info(
			"\n\n[AsyncImageAnalyzer] : Update Score Done {} - score type {}\n",
			imageLocationId, updateScoreType
		);
	}

	private boolean isScoreNotInitialized(Long imageLocationId) {
		return this.memoForOnProcessingTasks.contains(imageLocationId);
	}
}
