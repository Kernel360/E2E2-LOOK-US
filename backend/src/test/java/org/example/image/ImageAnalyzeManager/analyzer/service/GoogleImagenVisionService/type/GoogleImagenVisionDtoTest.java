package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.type;

// Create an instance of ClothDetection with valid ClothType, clothName, and normalizedVertices

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.type.GoogleImagenVisionDto.ClothDetection;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.junit.jupiter.api.Test;

import com.google.cloud.vision.v1.NormalizedVertex;

class GoogleImagenVisionDtoTest {

	@Test
	public void test_create_cloth_detection_with_valid_data() {
		ClothType clothType = ClothType.TOP;
		String clothName = "T-Shirt";
		List<NormalizedVertex> vertices = List.of(
			NormalizedVertex.newBuilder().setX(0.1f).setY(0.2f).build(),
			NormalizedVertex.newBuilder().setX(0.3f).setY(0.4f).build()
		);

		ClothDetection clothDetection = new ClothDetection(clothType, clothName, vertices);

		assertEquals(clothType, clothDetection.clothType());
		assertEquals(clothName, clothDetection.clothName());
		assertEquals(vertices, clothDetection.normalizedVertices());
	}
}