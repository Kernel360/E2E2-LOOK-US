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

	/**
	 * Compare the input RGB color with the LAB colors defined in LabColorCode enum
	 * and return a sorted list of LabColorCode based on similarity.
	 *
	 * @param red   The red component of the input RGB color.
	 * @param green The green component of the input RGB color.
	 * @param blue  The blue component of the input RGB color.
	 * @return A sorted list of LabColorCode based on similarity.
	 */
	public static List<ColorDistance> getSortedLabColors(int red, int green, int blue) {
		float[] rgbLab = ColorConverter.RGBtoLAB(red, green, blue);
		List<LabColorCode> colorList = List.of(LabColorCode.values());

		return colorList.stream()
			.map(color -> {
				double distance = calculateEuclideanDistance(
					rgbLab,
					new float[]{color.getL(), color.getA(), color.getB()});
				return new ColorDistance(color, distance);
			})
			.sorted((cd1, cd2) -> Double.compare(cd1.getDistance(), cd2.getDistance()))
			.collect(Collectors.toList());
	}

	public static class ColorDistance {
		private LabColorCode color;
		private double distance;

		public ColorDistance(LabColorCode color, double distance) {
			this.color = color;
			this.distance = distance;
		}

		public LabColorCode getColor() {
			return color;
		}

		public double getDistance() {
			return distance;
		}

		@Override
		public String toString() {
			return color.name() + " (Distance: " + distance + ")";
		}
	}


}
