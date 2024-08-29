package org.example.image.ImageAnalyzeManager.analyzer.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.junit.jupiter.api.Test;

class ClothAnalyzeDataEntityTest {
	// Creating an instance with all required fields
	@Test
	public void test_create_instance_with_all_required_fields() {
		ClothType clothType = ClothType.TOP;
		String clothName = "T-Shirt";
		LabColor labColor = new LabColor(50.0f, 0.0f, 0.0f);
		RGBColor rgbColor = new RGBColor(255, 0, 0);
		List<NormalizedVertex2D> boundingBox = List.of(new NormalizedVertex2D(0.1f, 0.1f), new NormalizedVertex2D(0.9f, 0.9f));
		Long imageLocationId = 123L;

		ClothAnalyzeDataEntity entity = ClothAnalyzeDataEntity.builder()
			.clothType(clothType)
			.clothName(clothName)
			.labColor(labColor)
			.rgbColor(rgbColor)
			.boundingBox(boundingBox)
			.imageLocationId(imageLocationId)
			.build();

		assertNotNull(entity);
		assertEquals(clothType, entity.getClothType());
		assertEquals(clothName, entity.getClothName());
		assertEquals(labColor, entity.getLabColor());
		assertEquals(rgbColor, entity.getRgbColor());
		assertEquals(boundingBox, entity.getBoundingBox());
		assertEquals(imageLocationId, entity.getImageLocationId());
	}
}