package org.example.config.redis;
import static org.example.image.redis.tools.RgbColorSimilarity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.example.image.redis.domain.dto.ColorDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisInitializer implements CommandLineRunner {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final String HASH_KEY = "ColorHash";
	private static final String ZSET_KEY = "ColorZSet";
	private static final Integer STANDARD_DIST = 10;

	public RedisInitializer(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void run(String... args) throws JsonProcessingException {
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, int[]> colorHashList = new HashMap<>();

		Boolean zSetExists = Objects.requireNonNull(zSetOps.size(ZSET_KEY)).describeConstable().isEmpty();
		Boolean hashExists = redisTemplate.hasKey(HASH_KEY);

		if(Boolean.FALSE.equals(zSetExists) && Boolean.FALSE.equals(hashExists)){
			colorHashList.put("Tomato Cream", new int[] {197, 118, 68});
			colorHashList.put("Scalet Smile", new int[] {159, 36, 54});
			colorHashList.put("Golden Palm", new int[] {170, 136, 5});
			colorHashList.put("Aventurine", new int[] {0, 83, 72});
			colorHashList.put("Red Orange", new int[] {240, 86, 39});
			colorHashList.put("Fern", new int[] {154, 160, 103});
			colorHashList.put("Italian Plum", new int[] {83, 49, 70});
			colorHashList.put("Moonstruck", new int[] {194, 190, 182});
			colorHashList.put("Winter Sky", new int[] {169, 192, 203});
			colorHashList.put("Lucent White", new int[] {244, 247, 255});

			colorHashList.put("White Swan", new int[] {228, 215, 197});
			colorHashList.put("Raw Umber", new int[] {146, 112, 95});
			colorHashList.put("Dark Gull Gray", new int[] {98, 93, 93});
			colorHashList.put("Baritone Blue", new int[] {39, 40, 54});
			colorHashList.put("Buckthorn Brown", new int[] {167, 111, 31});
			colorHashList.put("Sunburn", new int[] { 179, 114, 86});
			colorHashList.put("Rain Forest", new int[] {22, 70, 63});
			colorHashList.put("Cherry Tomato", new int[] { 235, 60, 39});
			colorHashList.put("Starlight Blue", new int[] { 181, 206, 212});
			colorHashList.put("Pureed Pumpkin", new int[] { 195, 65, 33});

			colorHashList.put("Misted Yellow", new int[] { 218, 185, 101});
			colorHashList.put("Almond Milk", new int[] { 214, 207, 190});
			colorHashList.put("Eggplant", new int[] { 98, 63, 76});
			colorHashList.put("Wave Ride", new int[] { 45, 128, 167});
			colorHashList.put("Storm Front", new int[] { 120, 115, 118});
			colorHashList.put("Sheepskin", new int[] { 218, 181, 143});
			colorHashList.put("Pinecorne", new int[] { 98, 71, 59});
			colorHashList.put("Evening Blue", new int[] {42, 41, 62});
			colorHashList.put("Dark Shadows", new int[] { 74, 75, 77});
			colorHashList.put("Iguana", new int[] { 129, 132, 85});


			for(String colorName : colorHashList.keySet()){
				int[] colorRGB = colorHashList.get(colorName);

				ColorDto.ColorSaveDtoRequest colorSaveDtoRequest
					= new ColorDto.ColorSaveDtoRequest(
					colorRGB[0], colorRGB[1], colorRGB[2],
					colorRGB[0], colorRGB[1], colorRGB[2]
				);
				// Serialization
				String colorSaveDtoJson = objectMapper.writeValueAsString(colorSaveDtoRequest);

				// check Hash if similar color already exists
				boolean check = true;
				for(String storedColorName : hashOps.keys(HASH_KEY)){
					Object storedColor = hashOps.get(HASH_KEY, storedColorName);
					JsonNode rootNode = objectMapper.readTree(storedColor.toString());
					int[] storedColorRGB = new int[] {
						rootNode.get("r").asInt(),
						rootNode.get("g").asInt(),
						rootNode.get("b").asInt()
					};

					if(calculateEuclideanDistance(colorRGB, storedColorRGB) <= STANDARD_DIST){
						check = false;
						log.warn("Can't stored {} Because similar color {} is already stored. ", colorName,
							storedColorName);
						break;
					}
				}
				if(check) {
					// ZSet Initialize
					zSetOps.add(ZSET_KEY, colorName, 0);
					// Hash Initialize
					hashOps.put(HASH_KEY, colorName, colorSaveDtoJson);
				}
			}
		}

	}
}
