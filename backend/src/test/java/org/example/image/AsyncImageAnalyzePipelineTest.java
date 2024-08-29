package org.example.image;

import static org.example.image.AsyncImageAnalyzePipeline.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.redis.service.ImageRedisService;
import org.example.post.repository.custom.UpdateScoreType;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.netty.handler.timeout.TimeoutException;

class AsyncImageAnalyzePipelineTest {
	// analyze method processes image analysis and updates score correctly
	@Test
	public void test_analyze_processes_image_analysis_and_updates_score_correctly() throws Exception {
		// Arrange
		Long imageLocationId = 1L;
		ImageAnalyzeManager imageAnalyzeManager = mock(ImageAnalyzeManager.class);
		ImageRedisService imageRedisService = mock(ImageRedisService.class);
		AsyncImageAnalyzePipeline pipeline = new AsyncImageAnalyzePipeline(imageAnalyzeManager, imageRedisService);

		doNothing().when(imageAnalyzeManager).analyze(imageLocationId);
		when(imageRedisService.saveNewColor(imageLocationId)).thenReturn(List.of("Red", "Blue"));

		// Act
		pipeline.analyze(imageLocationId);

		// Assert
		verify(imageAnalyzeManager, timeout(2000)).analyze(imageLocationId);
		verify(imageRedisService, timeout(2000)).saveNewColor(imageLocationId);
	}

	// updateScore updates the score in Redis when the score is initialized
	@Test
	public void test_update_score_when_initialized() throws JsonProcessingException {
		// Arrange
		Long imageLocationId = 1L;
		UpdateScoreType updateScoreType = UpdateScoreType.LIKE;
		ImageRedisService imageRedisService = mock(ImageRedisService.class);
		ImageAnalyzeManager imageAnalyzeManager = mock(ImageAnalyzeManager.class);
		AsyncImageAnalyzePipeline pipeline = new AsyncImageAnalyzePipeline(imageAnalyzeManager, imageRedisService);

		// Act
		pipeline.updateScore(imageLocationId, updateScoreType);

		// Assert
		verify(imageRedisService, times(1)).updateZSetColorScore(imageLocationId, updateScoreType);
	}

	@Test
	public void test_update_score_when_updateZSetColorScore_throws_exception() throws JsonProcessingException {
		// Arrange
		Long imageLocationId = 1L;
		UpdateScoreType updateScoreType = UpdateScoreType.LIKE;
		ImageRedisService imageRedisService = mock(ImageRedisService.class);
		ImageAnalyzeManager imageAnalyzeManager = mock(ImageAnalyzeManager.class);
		AsyncImageAnalyzePipeline pipeline = spy(new AsyncImageAnalyzePipeline(imageAnalyzeManager, imageRedisService));

		// Mock
		when(pipeline.isScoreInitialized(imageLocationId)).thenReturn(true);
		doThrow(new JsonProcessingException("Update redis-score failure", (JsonLocation)null) {
		}).when(imageRedisService).updateZSetColorScore(imageLocationId, updateScoreType);

		// Act & Assert
		RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
			pipeline.updateScore(imageLocationId, updateScoreType);
		});

		assertTrue(thrownException.getMessage().contains("Update redis-score failure"));

		verify(imageRedisService, times(1)).updateZSetColorScore(imageLocationId, updateScoreType);
	}

	@Test
	public void test_update_score_when_not_initialized() {
		// Arrange
		Long imageLocationId = 1L;
		UpdateScoreType updateScoreType = UpdateScoreType.LIKE;
		ImageRedisService imageRedisService = mock(ImageRedisService.class);
		ImageAnalyzeManager imageAnalyzeManager = mock(ImageAnalyzeManager.class);
		AsyncImageAnalyzePipeline pipeline = spy(new AsyncImageAnalyzePipeline(imageAnalyzeManager, imageRedisService));

		// Mock
		when(pipeline.isScoreInitialized(imageLocationId)).thenReturn(false);

		// Act
		pipeline.updateScore(imageLocationId, updateScoreType);

		// Assert
		verify(pipeline, atLeastOnce()).updateScore(imageLocationId, updateScoreType);
	}

	// updateScore handles InterruptedException during the pending task
	@Test
	public void test_update_score_handles_interrupted_exception() {
		// Arrange
		Long imageLocationId = 1L;
		UpdateScoreType updateScoreType = UpdateScoreType.LIKE;
		ImageRedisService imageRedisService = mock(ImageRedisService.class);
		ImageAnalyzeManager imageAnalyzeManager = mock(ImageAnalyzeManager.class);
		AsyncImageAnalyzePipeline pipeline = spy(new AsyncImageAnalyzePipeline(imageAnalyzeManager, imageRedisService));

		// Act
		doThrow(new RuntimeException()).when(pipeline).updateScore(anyLong(), any(UpdateScoreType.class));

		// Assert
		assertThrows(RuntimeException.class, () -> {
			pipeline.updateScore(imageLocationId, updateScoreType);
		});
	}

}