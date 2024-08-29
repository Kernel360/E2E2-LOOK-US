package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils;

import org.junit.jupiter.api.Test;
import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils.ClothTypeMapper;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClothTypeMapperTest {
	@Test
	public void test_returns_correct_cloth_type_for_valid_tag() {
		String tag = "Jeans";
		Optional<ClothType> result = ClothTypeMapper.toCategory(tag);
		assertEquals(Optional.of(ClothType.PANTS), result);
	}

	@Test
	public void test_returns_empty_optional_for_empty_tag() {
		String tag = "";
		Optional<ClothType> result = ClothTypeMapper.toCategory(tag);
		assertEquals(Optional.empty(), result);
	}
}
