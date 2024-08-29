package org.example.image.ImageAnalyzeManager.analyzer.type;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClothTypeTest {

	@Test
	public void test_enum_values_match_korean_clothing_categories() {
		ClothType[] expectedValues = {
			ClothType.OUTER,
			ClothType.TOP,
			ClothType.PANTS,
			ClothType.DRESS,
			ClothType.SKIRT,
			ClothType.SHOE,
			ClothType.BAG,
			ClothType.ACCESSORY,
			ClothType.HAT
		};
		ClothType[] actualValues = ClothType.values();
		assertArrayEquals(expectedValues, actualValues);
	}



	@Test
	public void test_enum_valueOf_throws_exception_for_invalid_names() {
		assertThrows(IllegalArgumentException.class, () -> {
			ClothType.valueOf("INVALID_NAME");
		});
	}
}