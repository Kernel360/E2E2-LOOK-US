package org.example.image.redis.service;

import static org.example.image.ImageAnalyzeManager.analyzer.tools.ColorConverter.*;
import static org.example.image.redis.tools.RgbColorSimilarity.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

;

/**
 * The type Image redis service.
 */
@Service
@RequiredArgsConstructor
public class ImageRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final PostRepository postRepository;
	private final ImageAnalyzeManager imageAnalyzeManager;

	private static final String HASH_KEY = "ColorHash";
	private static final String ZSET_KEY = "ColorZSet";
	private static final String COLOR_DIST_CAL_ZSET_KEY = "ColorDistCalZSet";
	private static final Integer STANDARD_DIST = 10;
	private static final Integer WEIGHT_LDT = 3;
	private static final Integer WEIGHT_LIKE = 2;
	private static final Integer WEIGHT_VIEW = 1;

	public List<String> saveNewColor(Long imageLocationId) throws JsonProcessingException {
		List<String> colorNameList = new ArrayList<>();
		ColorApiClient client = new ColorApiClient();
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
		ObjectMapper objectMapper = new ObjectMapper();

		ImageAnalyzeData imageAnalyzeData = imageAnalyzeManager.getAnalyzedData(imageLocationId);

		for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {        // Clothes from an image
			int[] colorRGB = {
				clothAnalyzeData.rgbColor().getRed(),
				clothAnalyzeData.rgbColor().getGreen(),
				clothAnalyzeData.rgbColor().getBlue()
			};
			if (calcCloseColorsDist(colorRGB, 1) == null
				|| calcCloseColorsDist(colorRGB, 1).get(0).distance() > STANDARD_DIST) {

				String hexColor = RGBtoHEX(colorRGB[0], colorRGB[1], colorRGB[2]);

				// thecolorapi API for get color name as string
				String colorName = client.getColorInfo(hexColor);

				// save new color
				if (hashOps.get(HASH_KEY, colorName) == null) {
					colorNameList.add(colorName);

					ColorDto.ColorSaveDtoRequest colorSaveDtoRequest
						= new ColorDto.ColorSaveDtoRequest(
						colorRGB[0], colorRGB[1], colorRGB[2],
						colorRGB[0], colorRGB[1], colorRGB[2]
					);

					// Serialization
					String colorSaveDtoJson = objectMapper.writeValueAsString(colorSaveDtoRequest);

					hashOps.put(HASH_KEY, colorName, colorSaveDtoJson);

					// save only color name to Redis by using ZSet
					zSetOps.add(ZSET_KEY, colorName, 0);
				}
			}
		}

		return colorNameList;
	}

	public List<String> updateExistingColor(PostPopularSearchCondition postPopularSearchCondition)
		throws JsonProcessingException, RuntimeException {
		List<String> savedColorNameList = new ArrayList<>();
		HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
		ObjectMapper objectMapper = new ObjectMapper();

		List<PostEntity> postEntities = postRepository.findAllByPostStatusAndCreatedAtAfterAndLikeCountGreaterThanEqualAndHitsGreaterThanEqual(
			PostStatus.PUBLISHED,
			postPopularSearchCondition.getCreatedAt(),
			postPopularSearchCondition.getLikeCount(),
			postPopularSearchCondition.getViewCount()
		);

		// get popular color of popular ClothAnalyzeData's image analyze data to change RGB
		HashMap<String, ColorDto.ColorSelectedDtoRequest> selectedColorHashMap = new HashMap<>();
		for (PostEntity post : postEntities) {
			ImageAnalyzeData imageAnalyzeData = imageAnalyzeManager.getAnalyzedData(post.getImageLocationId());

			// Selection Colors for updating because we can get several requests about same color
			for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {
				int[] colorRGB = {
					clothAnalyzeData.rgbColor().getRed(),
					clothAnalyzeData.rgbColor().getGreen(),
					clothAnalyzeData.rgbColor().getBlue()
				};

				ColorDto.ColorDistanceResponse colorDistanceResponse = calcCloseColorsDist(colorRGB, 1).get(0);
				String name = colorDistanceResponse.name();

				PostPopularSearchCondition condition = new PostPopularSearchCondition();
				condition.setCreatedAt(post.getCreatedAt());
				condition.setLikeCount(post.getLikeCount());
				condition.setViewCount(post.getHits());

				ColorDto.ColorSelectedDtoRequest colorSelectedDtoRequest = new ColorDto.ColorSelectedDtoRequest(
					post.getPostId(),
					colorDistanceResponse.distance(),
					colorRGB[0], colorRGB[1], colorRGB[2],
					condition
				);
				// Get the existing color request for the given name
				ColorDto.ColorSelectedDtoRequest existingColorRequest = selectedColorHashMap.get(name);

				// Check if the existingColorRequest is not null before accessing its condition
				if (existingColorRequest != null) {
					// Compare scores only if existingColorRequest is not null
					if (calcScoreOfPost(existingColorRequest.condition()) < calcScoreOfPost(
						postPopularSearchCondition)) {
						selectedColorHashMap.put(name, colorSelectedDtoRequest);
					}
				} else {
					// If no existing request found, simply put the new colorSelectedDtoRequest
					selectedColorHashMap.put(name, colorSelectedDtoRequest);
				}
			}
		}

		// Followed logic can be changed to get color more than 1 and compare all dist for colorRGB
		// and update color like make 3 color as 1 color
		// If you do that, must update return type as List

		// Update selected Colors to ColorHash
		for (String name : selectedColorHashMap.keySet()) {
			// color for update
			ColorDto.ColorSelectedDtoRequest colorSelectedDtoRequest = selectedColorHashMap.get(name);
			int[] colorRGB = new int[] {
				colorSelectedDtoRequest.r(),
				colorSelectedDtoRequest.g(),
				colorSelectedDtoRequest.b()
			};

			// Check whether new color is enough close to originRGB
			Object storedColor = hashOps.get(HASH_KEY, name);

			// JSON Parsing
			JsonNode rootNode = objectMapper.readTree(storedColor.toString());

			int[] storedColorRGB = new int[] {
				rootNode.get("r").asInt(),
				rootNode.get("g").asInt(),
				rootNode.get("b").asInt()
			};

			// Assuming storedColor is a JSON string, not a ColorSaveDtoRequest object
			String colorJson = (String)storedColor;

			// Convert JSON string to ColorSaveDtoRequest object
			ColorDto.ColorSaveDtoRequest storedColorRequest = objectMapper.readValue(colorJson,
				ColorDto.ColorSaveDtoRequest.class);

			// Update the ColorSaveDtoRequest object with new RGB values
			ColorDto.ColorSaveDtoRequest updatedColorRequest = ColorDto.ColorSaveDtoRequest.update(
				storedColorRequest, colorRGB[0], colorRGB[1], colorRGB[2]
			);

			// Serialize the updated ColorSaveDtoRequest object to JSON
			String colorSaveDtoJson = objectMapper.writeValueAsString(updatedColorRequest);

			if (calculateEuclideanDistance(colorRGB, storedColorRGB) <= STANDARD_DIST) {
				savedColorNameList.add(name);
				hashOps.put(HASH_KEY, name, colorSaveDtoJson);
			}

		}

		return savedColorNameList;
	}

	public void updateZSetColorScore(Long imageLocationId, UpdateScoreType updateScoreType) throws JsonProcessingException {
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

		ImageAnalyzeData imageAnalyzeData = imageAnalyzeManager.getAnalyzedData(imageLocationId);
		for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {
			int[] rgbColor = {
				clothAnalyzeData.rgbColor().getRed(),
				clothAnalyzeData.rgbColor().getBlue(),
				clothAnalyzeData.rgbColor().getGreen()
			};
			String colorName = calcCloseColorsDist(rgbColor, 1).get(0).name();

			Double currentScore = zSetOps.score(ZSET_KEY, colorName);
			if(currentScore == null){
				currentScore = 0.0;
			}
			currentScore += updateScoreType.getValue();

			zSetOps.add(ZSET_KEY, colorName, currentScore);
		}
	}

	public List<ColorDto.ColorDistanceResponse> calcCloseColorsDist(int[] rgb, int num) throws JsonProcessingException {
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		Map<Object, Object> colorMembers = hashOps.entries(HASH_KEY);
		ZSetOperations<String, Object> closestColorZSet = redisTemplate.opsForZSet();
		ObjectMapper objectMapper = new ObjectMapper();
		redisTemplate.delete(COLOR_DIST_CAL_ZSET_KEY);

		for (Map.Entry<Object, Object> entry : colorMembers.entrySet()) {
			String colorJson = (String)entry.getValue();
			String colorName = (String)entry.getKey();

			ColorDto.ColorUpdateDtoRequest originalRequest = objectMapper.readValue(colorJson,
				ColorDto.ColorUpdateDtoRequest.class);
			ColorDto.ColorUpdateDtoRequest colorUpdateDtoRequest = new ColorDto.ColorUpdateDtoRequest(
				colorName,
				originalRequest.r(),
				originalRequest.g(),
				originalRequest.b(),
				originalRequest.originR(),
				originalRequest.originG(),
				originalRequest.originB()
			);
			String colorUpdateJson = objectMapper.writeValueAsString(colorUpdateDtoRequest);

			double dist = calculateEuclideanDistance(
				rgb, new int[] {colorUpdateDtoRequest.r(), colorUpdateDtoRequest.g(), colorUpdateDtoRequest.b()}
			);

			closestColorZSet.add(COLOR_DIST_CAL_ZSET_KEY, colorUpdateJson, dist);
		}

		Set<ZSetOperations.TypedTuple<Object>> typedTuples
			= closestColorZSet.rangeWithScores(COLOR_DIST_CAL_ZSET_KEY, 0, num - 1);

		if (typedTuples == null || typedTuples.isEmpty()) {    // TODO: 수정 필요
			return null;
		}

		return typedTuples.stream()
			.map(tuple -> {
				String colorJson = (String)tuple.getValue(); // JSON 문자열 (ZSet value)
				double distance = tuple.getScore();           // 거리 (ZSet score)
				ColorDto.ColorUpdateDtoRequest colorRequest = null;
				try {
					colorRequest = objectMapper.readValue(colorJson, ColorDto.ColorUpdateDtoRequest.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
				return new ColorDto.ColorDistanceResponse(colorRequest.name(), distance);
			})
			.toList();
	}

	public HashMap<int[], List<ColorDto.ColorDistanceResponse>> getCloseColorNameList(
		ImageAnalyzeData imageAnalyzeData) throws JsonProcessingException {
		HashMap<int[], List<ColorDto.ColorDistanceResponse>> colorInfoHashMap = new HashMap<>();

		for (ClothAnalyzeData clothAnalyzeData : imageAnalyzeData.clothAnalyzeDataList()) {
			int[] colorRGB = {
				clothAnalyzeData.rgbColor().getRed(),
				clothAnalyzeData.rgbColor().getGreen(),
				clothAnalyzeData.rgbColor().getBlue()
			};
			colorInfoHashMap.put(colorRGB, calcCloseColorsDist(colorRGB, 10));
		}

		return colorInfoHashMap;
	}

	public double calcScoreOfPost(PostPopularSearchCondition condition) {
		double score = 0.0;

		Duration duration = Duration.between(condition.getCreatedAt(), LocalDateTime.now());
		long durationDays = duration.toDays();
		score += (12.0 - (durationDays / 7.0)) * WEIGHT_LDT;    // 3 month over post can't get score about duration

		int likeCount = condition.getLikeCount();
		score += likeCount * WEIGHT_LIKE;

		int viewCount = condition.getViewCount();
		score += viewCount * WEIGHT_VIEW;

		return score;
	}

}
