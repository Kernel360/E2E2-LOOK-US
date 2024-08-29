package org.example.image.ImageAnalyzeManager.analyzer.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LabColorSimilarityTest {
	// Calculate Euclidean distance between two valid LAB color arrays
	@Test
	public void test_euclidean_distance_valid_lab_colors() {
		float[] lab1 = {50.0f, 50.0f, 50.0f};
		float[] lab2 = {60.0f, 60.0f, 60.0f};
		double expectedDistance = Math.sqrt(300);
		double actualDistance = LabColorSimilarity.calculateEuclideanDistance(lab1, lab2);
		assertEquals(expectedDistance, actualDistance, 0.0001);
	}

	// Handle LAB color arrays with negative values
	@Test
	public void test_euclidean_distance_negative_lab_colors() {
		float[] lab1 = {-10.0f, -20.0f, -30.0f};
		float[] lab2 = {-40.0f, -50.0f, -60.0f};
		double expectedDistance = Math.sqrt(2700);
		double actualDistance = LabColorSimilarity.calculateEuclideanDistance(lab1, lab2);
		assertEquals(expectedDistance, actualDistance, 0.0001);
	}
}