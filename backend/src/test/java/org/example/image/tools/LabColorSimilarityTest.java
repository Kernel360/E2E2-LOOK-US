package org.example.image.tools;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LabColorSimilarityTest {

	@Test
	@DisplayName("Lab Color 테스트 : WHITE")
	void testGetSortedLabColorsWithWhite() {
		int red = 255;
		int green = 255;
		int blue = 255;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for WHITE: " + sortedColors);

		// WHITE should be the most similar color
		assertEquals(LabColorCode.WHITE, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : BLACK")
	void testGetSortedLabColorsWithBlack() {
		int red = 0;
		int green = 0;
		int blue = 0;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for BLACK: " + sortedColors);

		// BLACK should be the most similar color
		assertEquals(LabColorCode.BLACK, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : GRAY")
	void testGetSortedLabColorsWithGray() {
		int red = 128;
		int green = 128;
		int blue = 128;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for GRAY: " + sortedColors);

		// GRAY should be the most similar color
		assertEquals(LabColorCode.GRAY, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : RED")
	void testGetSortedLabColorsWithRed() {
		int red = 255;
		int green = 0;
		int blue = 0;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for RED: " + sortedColors);

		// RED should be the most similar color
		assertEquals(LabColorCode.RED, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : DARK_RED")
	void testGetSortedLabColorsWithDarkRed() {
		int red = 139;
		int green = 0;
		int blue = 0;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for DARK_RED: " + sortedColors);

		// DARK_RED should be the most similar color
		assertEquals(LabColorCode.DARK_RED, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : PINK")
	void testGetSortedLabColorsWithPink() {
		int red = 255;
		int green = 192;
		int blue = 203;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for PINK: " + sortedColors);

		// PINK should be the most similar color
		assertEquals(LabColorCode.PINK, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : ORANGE")
	void testGetSortedLabColorsWithOrange() {
		int red = 255;
		int green = 165;
		int blue = 0;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for ORANGE: " + sortedColors);

		// ORANGE should be the most similar color
		assertEquals(LabColorCode.ORANGE, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : YELLOW")
	void testGetSortedLabColorsWithYellow() {
		int red = 255;
		int green = 255;
		int blue = 0;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for YELLOW: " + sortedColors);

		// YELLOW should be the most similar color
		assertEquals(LabColorCode.YELLOW, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : GREEN")
	void testGetSortedLabColorsWithGreen() {
		int red = 0;
		int green = 255;
		int blue = 0;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for GREEN: " + sortedColors);

		// GREEN should be the most similar color
		assertEquals(LabColorCode.GREEN, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : DARK_GREEN")
	void testGetSortedLabColorsWithDarkGreen() {
		int red = 1;
		int green = 50;
		int blue = 32;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for DARK_GREEN: " + sortedColors);

		// DARK_GREEN should be the most similar color
		assertEquals(LabColorCode.DARK_GREEN, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : LIGHT_GREEN")
	void testGetSortedLabColorsWithLightGreen() {
		int red = 144;
		int green = 238;
		int blue = 144;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for LIGHT_GREEN: " + sortedColors);

		// LIGHT_GREEN should be the most similar color
		assertEquals(LabColorCode.LIGHT_GREEN, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : BLUE")
	void testGetSortedLabColorsWithBlue() {
		int red = 0;
		int green = 0;
		int blue = 255;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for BLUE: " + sortedColors);

		// BLUE should be the most similar color
		assertEquals(LabColorCode.BLUE, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : NAVY")
	void testGetSortedLabColorsWithNavy() {
		int red = 0;
		int green = 0;
		int blue = 128;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for NAVY: " + sortedColors);

		// NAVY should be the most similar color
		assertEquals(LabColorCode.NAVY, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : SKY_BLUE")
	void testGetSortedLabColorsWithSkyBlue() {
		int red = 135;
		int green = 206;
		int blue = 235;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for SKY_BLUE: " + sortedColors);

		// SKY_BLUE should be the most similar color
		assertEquals(LabColorCode.SKY_BLUE, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : PURPLE")
	void testGetSortedLabColorsWithPurple() {
		int red = 128;
		int green = 0;
		int blue = 128;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for PURPLE: " + sortedColors);

		// PURPLE should be the most similar color
		assertEquals(LabColorCode.PURPLE, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : VIOLET")
	void testGetSortedLabColorsWithViolet() {
		int red = 127;
		int green = 0;
		int blue = 255;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for VIOLET: " + sortedColors);

		// VIOLET should be the most similar color
		assertEquals(LabColorCode.VIOLET, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : BROWN")
	void testGetSortedLabColorsWithBrown() {
		int red = 165;
		int green = 42;
		int blue = 42;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for BROWN: " + sortedColors);

		// BROWN should be the most similar color
		assertEquals(LabColorCode.BROWN, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : BEIGE")
	void testGetSortedLabColorsWithBeige() {
		int red = 245;
		int green = 245;
		int blue = 220;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for BEIGE: " + sortedColors);

		// BEIGE should be the most similar color
		assertEquals(LabColorCode.BEIGE, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : MAGENTA")
	void testGetSortedLabColorsWithMagenta() {
		int red = 255;
		int green = 0;
		int blue = 255;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for MAGENTA: " + sortedColors);

		// MAGENTA should be the most similar color
		assertEquals(LabColorCode.MAGENTA, sortedColors.get(0).getColor());
	}

	@Test
	@DisplayName("Lab Color 테스트 : CYAN")
	void testGetSortedLabColorsWithCyan() {
		int red = 0;
		int green = 255;
		int blue = 255;

		List<LabColorSimilarity.ColorDistance> sortedColors = LabColorSimilarity.getSortedLabColors(red, green, blue);
		System.out.println("Sorted Colors for CYAN: " + sortedColors);

		// CYAN should be the most similar color
		assertEquals(LabColorCode.CYAN, sortedColors.get(0).getColor());
	}
}
