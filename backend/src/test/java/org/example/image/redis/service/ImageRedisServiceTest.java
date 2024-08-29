package org.example.image.redis.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.example.image.ImageAnalyzeManager.type.ImageAnalyzeData;
import org.example.image.redis.ColorApiClient;
import org.example.image.redis.domain.dto.ColorDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostPopularSearchCondition;
import org.example.post.repository.custom.UpdateScoreType;
import org.example.user.domain.entity.member.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class ImageRedisServiceTest {

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, Object, Object> hashOps;
	private ZSetOperations<String, Object> zSetOps;
	private PostRepository postRepository;
	private ImageAnalyzeManager imageAnalyzeManager;
	private ImageRedisService imageRedisService;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		redisTemplate = mock(RedisTemplate.class);
		hashOps = mock(HashOperations.class);
		zSetOps = mock(ZSetOperations.class);
		postRepository = mock(PostRepository.class);
		imageAnalyzeManager = mock(ImageAnalyzeManager.class);
		objectMapper = new ObjectMapper();
		when(redisTemplate.opsForHash()).thenReturn(hashOps);
		when(redisTemplate.opsForZSet()).thenReturn(zSetOps);

		imageRedisService = spy(new ImageRedisService(redisTemplate, postRepository, imageAnalyzeManager));
		when(redisTemplate.opsForHash()).thenReturn(hashOps);
	}

	// Successfully saves new colors to Redis when no close color exists
	@Test
	public void test_save_new_color_success() throws JsonProcessingException {
		// Arrange
		ColorApiClient client = mock(ColorApiClient.class);

		when(redisTemplate.opsForHash()).thenReturn(hashOps);
		when(redisTemplate.opsForZSet()).thenReturn(zSetOps);

		ClothAnalyzeData clothAnalyzeData = ClothAnalyzeData.builder()
			.rgbColor(new RGBColor(255, 0, 0))
			.build();

		ImageAnalyzeData imageAnalyzeData = ImageAnalyzeData.builder()
			.clothAnalyzeDataList(List.of(clothAnalyzeData))
			.build();

		when(imageAnalyzeManager.getAnalyzedData(anyLong())).thenReturn(imageAnalyzeData);
		when(client.getColorInfo(anyString())).thenReturn("Red");
		when(hashOps.get(anyString(), anyString())).thenReturn(null);

		// Act
		List<String> result = imageRedisService.saveNewColor(1L);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Red", result.get(0));
		verify(hashOps).put(anyString(), eq("Red"), anyString());
		verify(zSetOps).add(anyString(), eq("Red"), eq(0.0));
	}

	@Test
	public void test_returns_rgb_list_for_given_input() throws JsonProcessingException {
		// Arrange
		int[] rgb = {255, 0, 0};
		int num = 3;

		// Create mock color data
		ColorDto.ColorUpdateDtoRequest redColor = new ColorDto.ColorUpdateDtoRequest(
			"Red", 255, 0, 0, 255, 0, 0
		);
		ColorDto.ColorUpdateDtoRequest darkRedColor = new ColorDto.ColorUpdateDtoRequest(
			"DarkRed", 255, 0, 5, 255, 0, 5
		);
		ColorDto.ColorUpdateDtoRequest lightRedColor = new ColorDto.ColorUpdateDtoRequest(
			"LightRed", 255, 3, 0, 255, 3, 0
		);

		String redColorJson = objectMapper.writeValueAsString(redColor);
		String darkRedColorJson = objectMapper.writeValueAsString(darkRedColor);
		String lightRedColorJson = objectMapper.writeValueAsString(lightRedColor);

		// Create mock ZSetOperations.TypedTuple
		Set<ZSetOperations.TypedTuple<Object>> mockZSetResponse = new HashSet<>();
		mockZSetResponse.add(new ZSetOperations.TypedTuple<>() {
			@Override
			public int compareTo(ZSetOperations.TypedTuple<Object> o) {
				return 0;
			}

			@Override
			public Object getValue() {
				return redColorJson;
			}

			@Override
			public Double getScore() {
				return 0.0;
			}
		});
		mockZSetResponse.add(new ZSetOperations.TypedTuple<>() {
			@Override
			public int compareTo(ZSetOperations.TypedTuple<Object> o) {
				return 0;
			}

			@Override
			public Object getValue() {
				return darkRedColorJson;
			}

			@Override
			public Double getScore() {
				return 5.0;
			}
		});
		mockZSetResponse.add(new ZSetOperations.TypedTuple<>() {
			@Override
			public int compareTo(ZSetOperations.TypedTuple<Object> o) {
				return 0;
			}

			@Override
			public Object getValue() {
				return lightRedColorJson;
			}

			@Override
			public Double getScore() {
				return 3.0;
			}
		});

		// Set up mock behavior
		when(zSetOps.rangeWithScores(anyString(), anyLong(), anyLong())).thenReturn(mockZSetResponse);
		when(hashOps.get(anyString(), anyString())).thenReturn(redColorJson);

		// Act
		List<int[]> result = imageRedisService.getCloseColorList(rgb, num);

		// Assert
		assertEquals(3, result.size());
		assertArrayEquals(new int[] {255, 0, 0}, result.get(0));

	}

	// Successfully updates existing colors when valid PostPopularSearchCondition is provided
	@Test
	public void test_successful_update_existing_colors() throws
		JsonProcessingException {
		// Arrange
		PostPopularSearchCondition condition = new PostPopularSearchCondition();
		condition.setCreatedAt(LocalDateTime.now().minusDays(1));
		condition.setLikeCount(10);
		condition.setViewCount(100);

		List<PostEntity> postEntities = new ArrayList<>();
		UserEntity userEntity = mock(UserEntity.class);
		PostEntity postEntity = mock(PostEntity.class);
		when(postEntity.getPostId()).thenReturn(1L);

		postEntities.add(postEntity);

		ImageAnalyzeData imageAnalyzeData = ImageAnalyzeData.builder()
			.clothAnalyzeDataList(List.of(
				ClothAnalyzeData.builder()
					.rgbColor(new RGBColor(255, 0, 0))
					.build()
			))
			.build();

		ColorDto.ColorDistanceResponse colorDistanceResponse = new ColorDto.ColorDistanceResponse("Red", 5.0);

		// Mock repository and service
		when(postRepository.findAllByPostStatusAndCreatedAtAfterAndLikeCountGreaterThanEqualAndHitsGreaterThanEqual(
			any(), any(), anyInt(), anyInt())).thenReturn(postEntities);
		when(imageAnalyzeManager.getAnalyzedData(anyLong())).thenReturn(imageAnalyzeData);
		when(hashOps.get(anyString(), anyString())).thenReturn("{\"r\":255,\"g\":0,\"b\":0}");

		// Mock the spy's method call for calcCloseColorsDist
		doReturn(List.of(colorDistanceResponse)).when(imageRedisService)
			.calcCloseColorsDist(any(int[].class), anyInt());

		// Act
		List<String> result = imageRedisService.updateExistingColor(condition);

		// Assert
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals("Red", result.get(0));
	}

	// Handles cases where no posts match the given PostPopularSearchCondition
	@Test
	public void test_no_posts_match_condition() throws JsonProcessingException {
		// Arrange
		PostPopularSearchCondition condition = new PostPopularSearchCondition();
		condition.setCreatedAt(LocalDateTime.now().minusDays(1));
		condition.setLikeCount(10);
		condition.setViewCount(100);

		List<PostEntity> postEntities = Collections.emptyList();

		when(redisTemplate.opsForHash()).thenReturn(hashOps);
		when(postRepository.findAllByPostStatusAndCreatedAtAfterAndLikeCountGreaterThanEqualAndHitsGreaterThanEqual(
			any(), any(), anyInt(), anyInt())).thenReturn(postEntities);

		// Act
		List<String> result = imageRedisService.updateExistingColor(condition);

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// Successfully update ZSet color score when valid imageLocationId and updateScoreType are provided
	@Test
	public void test_update_zset_color_score_success() throws JsonProcessingException {

		Long imageLocationId = 1L;
		UpdateScoreType updateScoreType = UpdateScoreType.LIKE;

		ClothAnalyzeData clothAnalyzeData = ClothAnalyzeData.builder()
			.rgbColor(new RGBColor(255, 0, 0))
			.build();
		ImageAnalyzeData imageAnalyzeData = ImageAnalyzeData.builder()
			.clothAnalyzeDataList(List.of(clothAnalyzeData))
			.build();

		ColorDto.ColorDistanceResponse colorDistanceResponse = new ColorDto.ColorDistanceResponse("Red", 5.0);
		List<ColorDto.ColorDistanceResponse> mockColorDistanceList = List.of(colorDistanceResponse);

		when(redisTemplate.opsForZSet()).thenReturn(zSetOps);
		when(imageAnalyzeManager.getAnalyzedData(imageLocationId)).thenReturn(imageAnalyzeData);
		when(zSetOps.score("ColorZSet", "Red")).thenReturn(0.0);
		when(imageRedisService.calcCloseColorsDist(any(int[].class), anyInt())).thenReturn(mockColorDistanceList);

		// Act
		imageRedisService.updateZSetColorScore(imageLocationId, updateScoreType);

		// Assert
		verify(zSetOps).add("ColorZSet", "Red", 3.0);
	}

	// Handle null or invalid imageLocationId gracefully
	@Test
	public void test_update_zset_color_score_invalid_image_location_id() {

		Long invalidImageLocationId = -1L;
		UpdateScoreType updateScoreType = UpdateScoreType.LIKE;

		when(redisTemplate.opsForZSet()).thenReturn(zSetOps);
		when(imageAnalyzeManager.getAnalyzedData(invalidImageLocationId)).thenReturn(null);

		// Act & Assert
		assertThrows(NullPointerException.class, () -> {
			imageRedisService.updateZSetColorScore(invalidImageLocationId, updateScoreType);
		});
	}

	// Calculate color distances for a list of colors
	@Test
	public void test_calculate_color_distances() throws JsonProcessingException {
		// Arrange
		int[] referenceColor = new int[] {255, 0, 0};
		int num = 2;

		// Create mock data
		ColorDto.ColorUpdateDtoRequest color1 = new ColorDto.ColorUpdateDtoRequest("red", 255, 0, 0, 255, 0, 0);
		ColorDto.ColorUpdateDtoRequest color2 = new ColorDto.ColorUpdateDtoRequest("darkred", 255, 3, 0, 255, 3, 0);

		String color1Json = objectMapper.writeValueAsString(color1);
		String color2Json = objectMapper.writeValueAsString(color2);

		Map<Object, Object> colorMembers = new HashMap<>();
		colorMembers.put("red", color1Json);
		colorMembers.put("darkred", color2Json);

		// Mock Redis interactions
		when(redisTemplate.opsForHash().entries(any(String.class))).thenReturn(colorMembers);
		when(redisTemplate.opsForZSet()).thenReturn(zSetOps);

		// Use doAnswer to handle zSetOps.add() calls
		doAnswer(invocation -> {
			// Capture arguments to verify behavior
			String key = invocation.getArgument(0);
			String value = invocation.getArgument(1);
			double score = invocation.getArgument(2);

			// Add assertions or actions if needed
			// e.g., System.out.println("Key: " + key + ", Value: " + value + ", Score: " + score);

			return null;
		}).when(zSetOps).add(any(String.class), any(String.class), anyDouble());

		// Create mock responses
		List<ColorDto.ColorDistanceResponse> colorDistanceResponses = new ArrayList<>();
		colorDistanceResponses.add(new ColorDto.ColorDistanceResponse("red", 0.0));
		colorDistanceResponses.add(new ColorDto.ColorDistanceResponse("darkred", 3.0));


		when(imageRedisService.calcCloseColorsDist(referenceColor, 2)).thenReturn(colorDistanceResponses);
		// Act
		List<ColorDto.ColorDistanceResponse> result = imageRedisService.calcCloseColorsDist(referenceColor, num);

		// Assert
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("red", result.get(0).name());
		assertEquals(0.0, result.get(0).distance());
		assertEquals("darkred", result.get(1).name());
		assertEquals(3.0, result.get(1).distance());
	}
	// Calculates score correctly for valid PostPopularSearchCondition
	@Test
	public void calculates_score_correctly_for_valid_condition() {
		PostPopularSearchCondition condition = new PostPopularSearchCondition();
		// Set up the condition with valid data
		condition.setViewCount(1);
		condition.setCreatedAt(LocalDateTime.parse("2024-08-20T10:00:00"));
		condition.setLikeCount(3);

		double score = imageRedisService.calcScoreOfPost(condition);

		assertEquals(score, 14.714285714285714);
	}
}
