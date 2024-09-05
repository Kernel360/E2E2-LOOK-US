package org.example.image.redis.service;

import static org.example.image.ImageAnalyzeManager.analyzer.tools.ColorConverter.*;
import static org.example.image.redis.tools.RgbColorSimilarity.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.config.log.LogExecution;
import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.type.ImageAnalyzeData;
import org.example.image.redis.ColorApiClient;
import org.example.image.redis.domain.dto.ColorDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostPopularSearchCondition;
import org.example.post.repository.custom.UpdateScoreType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageRedisService {
	private static final String HASH_KEY = "ColorHash";
	private static final String ZSET_KEY = "ColorZSet";
	private static final String COLOR_DIST_CAL_ZSET_KEY = "ColorDistCalZSet";
	private static final int STANDARD_DIST = 10;
	private static final int WEIGHT_LDT = 1;
	private static final int WEIGHT_LIKE = 1;
	private static final int WEIGHT_VIEW = 1;
	private final RedisTemplate<String, Object> redisTemplate;
	private final PostRepository postRepository;
	private final ImageAnalyzeManager imageAnalyzeManager;
	private final ColorApiClient colorApiClient;
	private final ObjectMapper objectMapper;

	// Methods called from external methods

	@LogExecution
	public List<String> saveNewColor(Long imageLocationId) throws JsonProcessingException {
		List<String> colorNameList = new ArrayList<>();
		ImageAnalyzeData imageAnalyzeData = imageAnalyzeManager.getAnalyzedData(imageLocationId);

		for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {
			int[] colorRGB = extractRGB(clothAnalyzeData);
			if (isNewColor(colorRGB)) {
				String colorName = getColorName(colorRGB);
				if (redisTemplate.opsForHash().get(HASH_KEY, colorName) == null) {
					colorNameList.add(colorName);
					saveColor(colorName, colorRGB);
				}
			}
		}

		return colorNameList;
	}

	@LogExecution
	public List<String> updateExistingColor(PostPopularSearchCondition postPopularSearchCondition)
		throws JsonProcessingException {
		List<String> savedColorNameList = new ArrayList<>();
		List<PostEntity> postEntities = getPopularPosts(postPopularSearchCondition);
		Map<String, ColorDto.ColorSelectedDtoRequest> selectedColorHashMap
			= selectColorsForUpdate(postEntities);

		for (Map.Entry<String, ColorDto.ColorSelectedDtoRequest> entry : selectedColorHashMap.entrySet()) {
			String colorName = entry.getKey();
			ColorDto.ColorSelectedDtoRequest colorSelectedDto = entry.getValue();
			if (updateColorIfCloseEnough(colorName, colorSelectedDto)) {
				savedColorNameList.add(colorName);
			}
		}

		return savedColorNameList;
	}

	@LogExecution
	public void updateZSetColorScore(Long imageLocationId, UpdateScoreType updateScoreType)
		throws JsonProcessingException {
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
		ImageAnalyzeData imageAnalyzeData = imageAnalyzeManager.getAnalyzedData(imageLocationId);

		for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {
			int[] rgbColor = extractRGB(clothAnalyzeData);
			String colorName = calcCloseColorsDist(rgbColor, 1).get(0).name();
			Double currentScore = zSetOps.score(ZSET_KEY, colorName);
			if (currentScore == null) {
				currentScore = 0.0;
			}
			currentScore += updateScoreType.getValue();
			zSetOps.add(ZSET_KEY, colorName, currentScore);
		}
	}

	public List<ColorDto.ColorPopularResponse> getPopularColorList() {
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

		Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSetOps.reverseRangeWithScores(ZSET_KEY, 0, 9);
		if (typedTuples == null)
			return Collections.emptyList();

		return typedTuples.stream()
			.map(tuple -> {
				String colorName = (String)tuple.getValue();
				try {
					JsonNode rootNode = objectMapper.readTree(hashOps.get(HASH_KEY, colorName));
					int[] rgb = {rootNode.get("r").asInt(), rootNode.get("g").asInt(), rootNode.get("b").asInt()};
					return new ColorDto.ColorPopularResponse(colorName, rgb[0], rgb[1], rgb[2]);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toList());
	}

	@LogExecution
	public List<int[]> getCloseColorList(int[] rgb, int num) throws JsonProcessingException {
		HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

		List<String> closestColorNames = calcCloseColorsDist(rgb, num).stream()
			.map(ColorDto.ColorDistanceResponse::name)
			.toList();

		return closestColorNames.stream()
			.map(color -> {
				try {
					JsonNode rootNode
						= objectMapper.readTree(Objects.requireNonNull(hashOps.get(HASH_KEY, color)).toString());
					return new int[] {
						rootNode.get("r").asInt(), rootNode.get("g").asInt(), rootNode.get("b").asInt()
					};
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toList());
	}

	// Only Internally Used Methods

	@LogExecution
	protected List<ColorDto.ColorDistanceResponse> calcCloseColorsDist(int[] rgb, int num)
		throws JsonProcessingException {
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		ZSetOperations<String, Object> closestColorZSet = redisTemplate.opsForZSet();

		redisTemplate.delete(COLOR_DIST_CAL_ZSET_KEY);

		Map<Object, Object> colorMembers = hashOps.entries(HASH_KEY);
		for (Map.Entry<Object, Object> entry : colorMembers.entrySet()) {
			String colorJson = entry.getValue().toString();
			String colorName = entry.getKey().toString();
			ColorDto.ColorUpdateDtoRequest originalRequest
				= objectMapper.readValue(colorJson, ColorDto.ColorUpdateDtoRequest.class);
			ColorDto.ColorUpdateDtoRequest colorUpdateDtoRequest = new ColorDto.ColorUpdateDtoRequest(
				colorName, originalRequest.r(), originalRequest.g(), originalRequest.b(),
				originalRequest.originR(), originalRequest.originG(), originalRequest.originB()
			);
			String colorUpdateJson = objectMapper.writeValueAsString(colorUpdateDtoRequest);
			double dist = calculateEuclideanDistance(
				rgb, new int[] {colorUpdateDtoRequest.r(), colorUpdateDtoRequest.g(), colorUpdateDtoRequest.b()}
			);
			closestColorZSet.add(COLOR_DIST_CAL_ZSET_KEY, colorUpdateJson, dist);
		}

		Set<ZSetOperations.TypedTuple<Object>> typedTuples
			= closestColorZSet.rangeWithScores(COLOR_DIST_CAL_ZSET_KEY, 0, num - 1);
		if (typedTuples == null || typedTuples.isEmpty()) {
			return null;
		}

		return typedTuples.stream()
			.map(tuple -> {
				String colorJson = (String)tuple.getValue();
				double distance = tuple.getScore();
				try {
					ColorDto.ColorUpdateDtoRequest colorRequest
						= objectMapper.readValue(colorJson, ColorDto.ColorUpdateDtoRequest.class);
					return new ColorDto.ColorDistanceResponse(colorRequest.name(), distance);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toList());
	}

	private Map<String, ColorDto.ColorSelectedDtoRequest> selectColorsForUpdate(List<PostEntity> postEntities)
		throws JsonProcessingException {
		Map<String, ColorDto.ColorSelectedDtoRequest> selectedColorHashMap = new HashMap<>();

		for (PostEntity post : postEntities) {
			ImageAnalyzeData imageAnalyzeData = imageAnalyzeManager.getAnalyzedData(post.getImageLocationId());
			for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {
				int[] colorRGB = extractRGB(clothAnalyzeData);
				ColorDto.ColorDistanceResponse colorDistanceResponse = calcCloseColorsDist(colorRGB, 1).get(0);
				String name = colorDistanceResponse.name();

				PostPopularSearchCondition condition = new PostPopularSearchCondition(post);
				ColorDto.ColorSelectedDtoRequest colorSelectedDto = new ColorDto.ColorSelectedDtoRequest(
					post.getPostId(), colorDistanceResponse.distance(), colorRGB[0], colorRGB[1], colorRGB[2], condition
				);

				selectedColorHashMap.compute
					(name, (k, v) ->
						(v == null || calcScoreOfPost(condition) > calcScoreOfPost(v.condition())) ? colorSelectedDto :
							v
					);
			}
		}

		return selectedColorHashMap;
	}

	private boolean updateColorIfCloseEnough(String colorName, ColorDto.ColorSelectedDtoRequest colorSelectedDto)
		throws JsonProcessingException {
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
		String storedColorJson = hashOps.get(HASH_KEY, colorName);
		if (storedColorJson == null)
			return false;

		ColorDto.ColorSaveDtoRequest storedColor = objectMapper.readValue(storedColorJson,
			ColorDto.ColorSaveDtoRequest.class);
		int[] storedColorRGB = {storedColor.r(), storedColor.g(), storedColor.b()};
		int[] newColorRGB = {colorSelectedDto.r(), colorSelectedDto.g(), colorSelectedDto.b()};

		if (calculateEuclideanDistance(newColorRGB, storedColorRGB) <= STANDARD_DIST) {
			ColorDto.ColorSaveDtoRequest updatedColor
				= ColorDto.ColorSaveDtoRequest.update(storedColor, newColorRGB[0], newColorRGB[1], newColorRGB[2]);
			hashOps.put(HASH_KEY, colorName, objectMapper.writeValueAsString(updatedColor));
			return true;
		}

		return false;
	}

	private int[] extractRGB(ClothAnalyzeData clothAnalyzeData) {
		return new int[] {
			clothAnalyzeData.rgbColor().getRed(),
			clothAnalyzeData.rgbColor().getGreen(),
			clothAnalyzeData.rgbColor().getBlue()
		};
	}

	private boolean isNewColor(int[] colorRGB) throws JsonProcessingException {
		List<ColorDto.ColorDistanceResponse> closeColors = calcCloseColorsDist(colorRGB, 1);
		return closeColors == null || closeColors.get(0).distance() > STANDARD_DIST;
	}

	private String getColorName(int[] colorRGB) {
		String hexColor = RGBtoHEX(colorRGB[0], colorRGB[1], colorRGB[2]);
		return colorApiClient.getColorInfo(hexColor);
	}

	private void saveColor(String colorName, int[] colorRGB) throws JsonProcessingException {
		ColorDto.ColorSaveDtoRequest colorSaveDto = new ColorDto.ColorSaveDtoRequest(
			colorRGB[0], colorRGB[1], colorRGB[2], colorRGB[0], colorRGB[1], colorRGB[2]
		);
		String colorSaveDtoJson = objectMapper.writeValueAsString(colorSaveDto);

		redisTemplate.opsForHash().put(HASH_KEY, colorName, colorSaveDtoJson);
		redisTemplate.opsForZSet().add(ZSET_KEY, colorName, 0);
	}

	private List<PostEntity> getPopularPosts(PostPopularSearchCondition condition) {
		return postRepository.findAllByPostStatusAndCreatedAtAfterAndLikeCountGreaterThanEqualAndHitsGreaterThanEqual(
			PostStatus.PUBLISHED, condition.getCreatedAt(), condition.getLikeCount(), condition.getViewCount()
		);
	}

	@LogExecution
	protected double calcScoreOfPost(PostPopularSearchCondition condition) {
		double score = 0.0;
		Duration duration = Duration.between(condition.getCreatedAt(), LocalDateTime.now());
		long durationDays = duration.toDays();
		score += (Math.max((12.0 - (durationDays / 7.0)), 0)) * WEIGHT_LDT;
		score += condition.getLikeCount() * WEIGHT_LIKE;
		score += condition.getViewCount() * WEIGHT_VIEW;
		return score;
	}
}