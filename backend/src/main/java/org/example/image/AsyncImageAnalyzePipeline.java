package org.example.image;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.redis.service.ImageRedisService;
import org.example.post.repository.custom.UpdateScoreType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *       [ 비동기 호출에 대한 해결 방법은 총 2가지입니다 ]
 *        -
 *       (1) 방법: updateZSetColorScore 함수 호출 전에 image분석 정보가 있는지 체크하고, 있을 때만 score를 update 합니다.
 *           한계: 이미지 분석이 아직 진행 중일 때 발생한 likes, scrap, view 업데이트는 score가 업데이트 되지 않습니다.
 *        -
 *       (2) 방법: job queue의 모든 작업 중에서 imageLocationId와 같은 이미지를 분석하려는 작업이 있는지 검사합니다.
 *                분석하려는 작업이 하나라도 큐에 있다면, 그것은 작업이 진행중인 상태일 것입니다.
 *                따라서 해당 작업을 다시 큐에 집어 넣고, 분석 쓰레드는 1초간 기다린 뒤 재수행합니다.
 *           한계: 동시에 1000개 사진이 업로드 된다면, 위 과정이 어떻게 처리될 지 고민이 필요합니다.
 *                - 싱글 쓰레드 방식이 적절한가?
 *                - 큐에 작업을 다시 넣고 1초간 기다리는 것이 효율적인가?
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncImageAnalyzePipeline {

	/**
	 * 이미지 분석이 끝나지 않은 상태 에서 게시물 조회 발생시, 잠시 대기 후 비동기 요청을 재시도 합니다.
	 * 분석이 종료되기 까지 평균 1.5초 소요됬던 점을 바탕으로 1000ms로 설정 했습니다.
	 */
	private static final int UPDATE_SCORE_PENDING_WAIT_MS = 1000;
	protected static final int UPDATE_SCORE_MAX_TIMEOUT_MS = 5000;

	private final ImageAnalyzeManager imageAnalyzeManager;
	private final ImageRedisService imageRedisService;

	/** ------------------------ (1) Multi Threaded ------------------------------------------
	 *  @implNote  Must Test concurrency problem, if executor is not single threaded.
	 *  @link      <a href="https://stackoverflow.com/questions/24981551/concurrency-on-treeset-using-last-method">
	 *               Stack-Overflow : concurrency-on-treeset
	 *             </a>
	 */
	// Executor ThreadPoolExecutor = Executors.newFixedThreadPool( 8 );
	// private final Set<Long> onGoingTasks = Collections.synchronizedSortedSet(new TreeSet<>());

	/** ------------------------ (2) Single Threaded ------------------------------------------
	 *  @implNote  No synchronization required when accessing objects that are not thread-safe.
	 *  @link      <a href="https://www.baeldung.com/java-completablefuture-threadpool">
	 *                baeldung.com - threadpool with completablefuture
	 *             </a>
	 */
	// Default folkJoinPool을 사용하는 것으로 변경합니다.
	// private final Executor singleThreadExecutor = Executors.newSingleThreadExecutor(); // 큐 + 싱글 쓰레드
	private final Set<Long> onGoingTasks = new HashSet<>(); // 현재 처리 중인 작업을 임시 기록 합니다.

	/**
	 * @apiNote [이미지 분석]을 분석 쓰레드에게 비동기로 요청합니다.
	 * @param imageLocationId 분석하길 원하는 이미지-위치 Id
	 */
	// @LogExecution
	public void analyze(Long imageLocationId) {
		CompletableFuture.runAsync(
			() -> onStartTask.accept(imageLocationId)
		).thenRunAsync(
			() -> extractClothAndColorTask.accept(imageAnalyzeManager, imageLocationId)
		).thenRunAsync(
			() -> colorScoringTask.accept(imageRedisService, imageLocationId)
		).whenCompleteAsync(
			(__, err) -> onCompleteTask.accept(err, imageLocationId)
		);
	}

	/**
	 * @apiNote [점수 업데이트]를 분석 쓰레드에게 비동기로 요청합니다.
	 * @param imageLocationId 분석하길 원하는 이미지-위치 Id
	 * @param updateScoreType 점수 업데이트의 종류 (ex. 좋아요 증가/감소)
	 */
	// @LogExecution
	public void updateScore(Long imageLocationId, UpdateScoreType updateScoreType)  {

		if ( isScoreInitialized(imageLocationId) ) {
			try {
				this.imageRedisService.updateZSetColorScore(imageLocationId, updateScoreType);
				log.debug("Update redis-score done - image {} / type-{}\n", imageLocationId, updateScoreType);
			} catch (JsonProcessingException e) {
				log.error("Update redis-score failure - image {} / type-{}\n", imageLocationId, updateScoreType);
				throw new RuntimeException(e); // TODO: use custom exception
			}
			return;
		}

		CompletableFuture.runAsync(
			() -> pendingTask.accept(UPDATE_SCORE_PENDING_WAIT_MS)
		).thenRunAsync(
			() -> this.updateScore(imageLocationId, updateScoreType)
		).orTimeout( /* for safety, throw a TimeoutException in case of a timeout */
			UPDATE_SCORE_MAX_TIMEOUT_MS, TimeUnit.MILLISECONDS
		);
	}

	// --------------------------------------------------------------------------
	// Internal Methods
	protected boolean isScoreInitialized(Long imageLocationId) {
		return !this.onGoingTasks.contains(imageLocationId);

	}

	protected final Consumer<Integer> pendingTask = (ms) -> {
		try {
			log.debug("Pending task... - sleep {}ms", ms);
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			log.error("Pending task failure");
			throw new RuntimeException(e);
		}
	};

	private final Consumer<Long> onStartTask = (imageLocationId) -> {
		this.onGoingTasks.add(imageLocationId);
		log.debug("(0) Image analyze pipeline start - Image {}", imageLocationId);
	};

	private final BiConsumer<ImageAnalyzeManager, Long> extractClothAndColorTask = (analyzeManager, imageLocationId) -> {
		try {
			analyzeManager.analyze(imageLocationId);
			log.debug("(1) Extract cloth info done - image {}", imageLocationId);
		} catch (IOException e) {
			log.error("(1) Extract cloth info failure - image {}", imageLocationId);
			throw new RuntimeException(e);
		}
	};

	private final BiConsumer<ImageRedisService, Long> colorScoringTask = (redisService, imageLocationId) -> {
		try {
			var colorList = redisService.saveNewColor(imageLocationId);
			log.debug("(2) save new color list done - image {}, colorList {}", imageLocationId, colorList);
		} catch (JsonProcessingException e) {
			log.error("(2) save new color list failure - image {}", imageLocationId);
			throw new RuntimeException(e);
		}
	};

	private final BiConsumer<? super Throwable, Long> onCompleteTask = (exception, imageLocationId) -> {
		// remove from in-task record
		this.onGoingTasks.remove(imageLocationId);

		if (exception != null) {
			log.error("(3) Image analyze pipeline failure - image {}", imageLocationId);
			throw new RuntimeException(exception); // TODO: throw custom task exception
		}
		log.debug("(3) Image analyze pipeline done - image {}", imageLocationId);
	};
/*
	*//**
	 * @ deprecated 아직 구현되지 않은 기능입니다.
	 * 기존엔 옷 카테고리를 사용자가 직접 입력했습니다.
	 * 이를 사용자가 하지 않고, 이미지 분석 결과로 부터 자동으로 설정되도록 하고자 한 시도입니다.
	 * 추후에 구현이 될지 모르지만, 일단 코드를 남깁니다.
	 *//*
	private final BiConsumer<ImageAnalyzeManager, Long> mapAnalyzedClothCategoryWithPostTask = (analyzeManager, imageLocationId) -> {
		// 1. get cloth analyzed data
		List<ClothAnalyzeData> clothAnalyzeDataList
			= analyzeManager.getAnalyzedData(imageLocationId).clothAnalyzeDataList();
		// 2. set cloth category
		// 분석 결과를 가지고 Post Entity의 카테고리와 매핑합니다.
	};*/
}
