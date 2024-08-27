package org.example.image.ImageAnalyzeManager.analyzer.tools;

import java.util.List;
import java.util.stream.Collectors;

public class LabColorSimilarity {

	/**
	 * Calculate the Euclidean distance between two LAB colors.
	 *
	 * @param lab1 The first LAB color as a float array.
	 * @param lab2 The second LAB color as a float array.
	 * @return The Euclidean distance.
	 */
	private static double calculateEuclideanDistance(float[] lab1, float[] lab2) {
		return Math.sqrt(Math.pow(lab1[0] - lab2[0], 2) +
			Math.pow(lab1[1] - lab2[1], 2) +
			Math.pow(lab1[2] - lab2[2], 2));
	}
}
