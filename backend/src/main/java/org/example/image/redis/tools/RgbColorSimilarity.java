package org.example.image.redis.tools;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RgbColorSimilarity {
	/**
	 * Calculate the Euclidean distance between two RGB colors.
	 *
	 * @param rgb1 The first RGB color as an integer array [R, G, B].
	 * @param rgb2 The second RGB color as an integer array [R, G, B].
	 * @return The Euclidean distance.
	 */
	public static double calculateEuclideanDistance(int[] rgb1, int[] rgb2) {
		return Math.sqrt(Math.pow(rgb1[0] - rgb2[0], 2) +
			Math.pow(rgb1[1] - rgb2[1], 2) +
			Math.pow(rgb1[2] - rgb2[2], 2));
	}

}
