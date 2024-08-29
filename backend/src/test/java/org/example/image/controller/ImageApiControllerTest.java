package org.example.image.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.example.image.redis.service.ImageRedisService;
import org.example.post.repository.custom.PostPopularSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

class ImageApiControllerTest {

	// Successfully update colors in Redis when valid PostPopularSearchCondition is provided
	@Test
	public void test_update_color_to_redis_success() throws JsonProcessingException {
		// Arrange
		ImageRedisService imageRedisService = mock(ImageRedisService.class);
		ImageApiController imageApiController = new ImageApiController(imageRedisService);
		PostPopularSearchCondition condition = new PostPopularSearchCondition();
		condition.setCreatedAt(LocalDateTime.now().minusDays(1));
		condition.setLikeCount(10);
		condition.setViewCount(100);

		List<String> expectedColors = Arrays.asList("Red", "Blue");
		when(imageRedisService.updateExistingColor(condition)).thenReturn(expectedColors);

		// Act
		ResponseEntity<List<String>> response = imageApiController.updateColorToRedis(condition);

		// Assert
		assertEquals(
			HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedColors, response.getBody());
	}
}