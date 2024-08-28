package org.example.image;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.redis.service.ImageRedisService;
import org.example.log.LogExecution;
import org.example.post.repository.custom.UpdateScoreType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *  비동기 호출에 대한 해결 방법은 총 2가지입니다.
 *        -
 *       (1) 방법: updateZSetColorScore 함수 호출 전에 image분석 정보가 있는지 체크하고, 있을 때만 score를 update 합니다.
 *           한계: 이미지 분석이 아직 진행 중일 때 발생한 likes, scrap, view 업데이트는 score가 업데이트 되지 않습니다.
 *        -
 *       (2) 방법: job queue의 모든 작업 중에서 imageLocationId와 같은 이미지를 분석하려는 작업이 있는지 검사합니다.
 *                분석하려는 작업이 하나라도 큐에 있다면, 그것은 작업이 진행중인 상태일 것입니다.
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncImageAnalyzer {

	/**
	 * 이미지 분석이 끝나지 않은 상태 에서 게시물 조회 발생시, 잠시 대기 후 비동기 요청을 재시도 합니다.
	 * 분석이 종료되기 까지 평균 1.5초 소요됬던 점을 바탕으로 1000ms로 설정 했습니다.
	 */
	private static final int UPDATE_SCORE_PENDING_WAIT_MS = 1000;
	private static final int UPDATE_SCORE_MAX_TIMEOUT_MS = 5000;

	private final ImageAnalyzeManager imageAnalyzeManager;
	private final ImageRedisService imageRedisService;

	/** ------------------------ (1) Multi Threaded ------------------------------------------
	 *  @implNote  Must Test concurrency problem, if executor is not single threaded.
	 *  @link      <a href="https://stackoverflow.com/questions/24981551/concurrency-on-treeset-using-last-method">
	 *               Stack-Overflow : concurrency-on-treeset
	 *             </a>
	 */
	// private final Set<Long> memoForOnProcessingTasks = Collections.synchronizedSortedSet(new TreeSet<>());
	// Executor scheduledThreadPoolExecutor = Executors.newScheduledThreadPool(10);

	/** ------------------------ (2) Single Threaded ------------------------------------------
	 *  @implNote  No synchronization required when accessing objects that are not thread-safe.
	 *  @link      <a href="https://www.baeldung.com/java-completablefuture-threadpool">
	 *                baeldung.com - threadpool with completablefuture
	 *             </a>
	 */
	private final Executor singleThreadExecutor = Executors.newSingleThreadExecutor();
	private final Set<Long> onGoingTasks = new HashSet<>(); // 현재 처리 중인 작업을 임시 기록 합니다.

	/**
	 * @apiNote [이미지 분석]을 분석 쓰레드에게 비동기로 요청합니다.
	 * @param imageLocationId 분석하길 원하는 이미지-위치 Id
	 */
  @LogExecution
	public void requestImageAnalyzeAsync(Long imageLocationId) {
		CompletableFuture.runAsync(
			() -> onStartTask.accept(imageLocationId), singleThreadExecutor
		).thenRunAsync(
			() -> extractClothAndColorTask.accept(imageAnalyzeManager, imageLocationId), singleThreadExecutor
		).thenRunAsync(
			() -> colorScoringTask.accept(imageRedisService, imageLocationId), singleThreadExecutor
		).whenCompleteAsync(
			(__, err) -> onCompleteTask.accept(err, imageLocationId), singleThreadExecutor
		);
	}

	/**
	 * @apiNote [점수 업데이트]를 분석 쓰레드에게 비동기로 요청합니다.
	 * @param imageLocationId 분석하길 원하는 이미지-위치 Id
	 * @param updateScoreType 점수 업데이트의 종류 (ex. 좋아요 증가/감소)
	 */
	public void requestScoreUpdateAsync(Long imageLocationId, UpdateScoreType updateScoreType)  {

		if ( isScoreInitialized(imageLocationId) ) {
			try {
				this.imageRedisService.updateZSetColorScore(imageLocationId, updateScoreType);
				log.info("\n\n[AsyncImageAnalyzer] : Update-{} type-{}\n", imageLocationId, updateScoreType);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e); // TODO: use custom exception
			}
			return;
		}

		CompletableFuture.runAsync(
			() -> pendingTask.accept(UPDATE_SCORE_PENDING_WAIT_MS), singleThreadExecutor
		).thenRunAsync(
			() -> this.requestScoreUpdateAsync(imageLocationId, updateScoreType), singleThreadExecutor
		).orTimeout( /* for safety, throw a TimeoutException in case of a timeout */
			UPDATE_SCORE_MAX_TIMEOUT_MS, TimeUnit.MILLISECONDS
		);
	}

	// --------------------------------------------------------------------------
	// Internal Methods
  @LogExecution
	private boolean isScoreInitialized(Long imageLocationId) {
		return !this.onGoingTasks.contains(imageLocationId);

	}

	private final Consumer<Integer> pendingTask = (ms) -> {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	};

	private final Consumer<Long> onStartTask = (imageLocationId) -> {
		this.onGoingTasks.add(imageLocationId);
		log.info("\n\n[AsyncImageAnalyzer] : Image Analyze Task Starting... - Image {}\n", imageLocationId);
	};

	private final BiConsumer<? super Throwable, Long> onCompleteTask = (exception, imageLocationId) -> {
		this.onGoingTasks.remove(imageLocationId);
		log.info("\n\n[AsyncImageAnalyzer] : Update Score Async Finished - Image {}\n", imageLocationId);
		if (exception != null) {
			throw new RuntimeException(exception); // TODO: throw custom task exception
		}
	};

	private final BiConsumer<ImageAnalyzeManager, Long> extractClothAndColorTask = (analyzeManager, imageLocationId) -> {
		try {
			onGoingTasks.add(imageLocationId);
			analyzeManager.analyze(imageLocationId);
			log.info("\n\n[AsyncImageAnalyzer] : ------- (1) analyze image done - id image {}\n", imageLocationId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	};

	private final BiConsumer<ImageRedisService, Long> colorScoringTask = (redisService, imageLocationId) -> {
		try {
			var colorList = redisService.saveNewColor(imageLocationId);
			log.info("\n\n[AsyncImageAnalyzer] : ------- (2) save new color list done - image {}, colorList {}\n", imageLocationId, colorList);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	};
}
