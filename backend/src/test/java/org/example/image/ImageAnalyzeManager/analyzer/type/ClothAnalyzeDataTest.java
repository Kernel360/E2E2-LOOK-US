package org.example.image.ImageAnalyzeManager.analyzer.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClothAnalyzeDataTest {
	// Creating ClothAnalyzeData with valid ClothType, clothName, LabColor, RGBColor, NormalizedVertex2D, and tristimulus values
	@Test
	public void test_create_cloth_analyze_data_with_valid_values() {
		ClothType clothType = ClothType.TOP;
		String clothName = "T-Shirt";
		LabColor labColor = new LabColor(50.0f, 0.0f, 0.0f);
		RGBColor rgbColor = new RGBColor(255, 0, 0);
		NormalizedVertex2D leftTopVertex = new NormalizedVertex2D(0.1f, 0.1f);
		NormalizedVertex2D rightBottomVertex = new NormalizedVertex2D(0.9f, 0.9f);
		float[] tristimulus = {95.047f, 100.000f, 108.883f};

		ClothAnalyzeData clothAnalyzeData = ClothAnalyzeData.builder()
			.clothType(clothType)
			.clothName(clothName)
			.labColor(labColor)
			.rgbColor(rgbColor)
			.leftTopVertex(leftTopVertex)
			.rightBottomVertex(rightBottomVertex)
			.tristimulus(tristimulus)
			.build();

		assertNotNull(clothAnalyzeData);
		assertEquals(clothType, clothAnalyzeData.clothType());
		assertEquals(clothName, clothAnalyzeData.clothName());
		assertEquals(labColor, clothAnalyzeData.labColor());
		assertEquals(rgbColor, clothAnalyzeData.rgbColor());
		assertEquals(leftTopVertex, clothAnalyzeData.leftTopVertex());
		assertEquals(rightBottomVertex, clothAnalyzeData.rightBottomVertex());
		assertArrayEquals(tristimulus, clothAnalyzeData.tristimulus());
	}
	// Handling null values for optional fields in ClothAnalyzeData
	@Test
	public void test_handle_null_values_in_cloth_analyze_data() {
		ClothType clothType = ClothType.TOP;
		String clothName = "T-Shirt";
		LabColor labColor = null;
		RGBColor rgbColor = null;
		NormalizedVertex2D leftTopVertex = null;
		NormalizedVertex2D rightBottomVertex = null;
		float[] tristimulus = null;

		ClothAnalyzeData clothAnalyzeData = ClothAnalyzeData.builder()
			.clothType(clothType)
			.clothName(clothName)
			.labColor(labColor)
			.rgbColor(rgbColor)
			.leftTopVertex(leftTopVertex)
			.rightBottomVertex(rightBottomVertex)
			.tristimulus(tristimulus)
			.build();

		assertNotNull(clothAnalyzeData);
		assertEquals(clothType, clothAnalyzeData.clothType());
		assertEquals(clothName, clothAnalyzeData.clothName());
		assertNull(clothAnalyzeData.labColor());
		assertNull(clothAnalyzeData.rgbColor());
		assertNull(clothAnalyzeData.leftTopVertex());
		assertNull(clothAnalyzeData.rightBottomVertex());
		assertNull(clothAnalyzeData.tristimulus());
	}
}