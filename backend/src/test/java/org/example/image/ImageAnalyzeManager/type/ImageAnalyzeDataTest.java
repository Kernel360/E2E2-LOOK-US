package org.example.image.ImageAnalyzeManager.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.junit.jupiter.api.Test;

class ImageAnalyzeDataTest {
	// Create ImageAnalyzeData with a non-empty list of ClothAnalyzeData
	@Test
	public void test_create_image_analyze_data_with_non_empty_list() {
		ClothAnalyzeData cloth1 = ClothAnalyzeData.builder()
			.clothType(ClothType.TOP)
			.clothName("Blue Shirt")
			.labColor(new LabColor(50F, 0F, 0F))
			.rgbColor(new RGBColor(0, 0, 255))
			.leftTopVertex(new NormalizedVertex2D(0.1f, 0.1f))
			.rightBottomVertex(new NormalizedVertex2D(0.5f, 0.5f))
			.tristimulus(new float[]{0.5f, 0.5f, 0.5f})
			.build();

		ClothAnalyzeData cloth2 = ClothAnalyzeData.builder()
			.clothType(ClothType.PANTS)
			.clothName("Black Pants")
			.labColor(new LabColor(30F, 0F, 0F))
			.rgbColor(new RGBColor(0, 0, 0))
			.leftTopVertex(new NormalizedVertex2D(0.6f, 0.6f))
			.rightBottomVertex(new NormalizedVertex2D(1.0f, 1.0f))
			.tristimulus(new float[]{0.3f, 0.3f, 0.3f})
			.build();

		List<ClothAnalyzeData> clothList = List.of(cloth1, cloth2);
		ImageAnalyzeData imageAnalyzeData = ImageAnalyzeData.builder()
			.clothAnalyzeDataList(clothList)
			.build();

		assertNotNull(imageAnalyzeData);
		assertEquals(2, imageAnalyzeData.clothAnalyzeDataList().size());
	}
}