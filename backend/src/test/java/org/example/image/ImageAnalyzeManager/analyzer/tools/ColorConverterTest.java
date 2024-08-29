package org.example.image.ImageAnalyzeManager.analyzer.tools;

import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ColorConverterTest {

	// Convert RGB to LAB color space accurately
	@Test
	public void test_rgb_to_lab_conversion_accuracy() {
		RGBColor rgbColor = new RGBColor(255, 0, 0);
		float[] tristimulus = {95.047f, 100.000f, 108.883f}; // D65 standard tristimulus
		LabColor labColor = ColorConverter.RGBtoLAB(rgbColor, tristimulus);

		assertEquals(53.23288f, labColor.getL(), 0.7);
		assertEquals(80.10933f, labColor.getA(), 0.7);
		assertEquals(67.22006f, labColor.getB(), 0.7);
	}

	// Handle RGB values at the boundary (0 and 255)
	@Test
	public void test_rgb_to_lab_boundary_values() {
		RGBColor rgbColorBlack = new RGBColor(0, 0, 0);
		RGBColor rgbColorWhite = new RGBColor(255, 255, 255);
		float[] tristimulus = {95.047f, 100.000f, 108.883f}; // D65 standard tristimulus

		LabColor labColorBlack = ColorConverter.RGBtoLAB(rgbColorBlack, tristimulus);
		LabColor labColorWhite = ColorConverter.RGBtoLAB(rgbColorWhite, tristimulus);

		assertEquals(0.0f, labColorBlack.getL(), 0.01);
		assertEquals(0.0f, labColorBlack.getA(), 0.01);
		assertEquals(0.0f, labColorBlack.getB(), 0.01);

		assertEquals(100.0f, labColorWhite.getL(), 0.01);
		assertEquals(0.0f, labColorWhite.getA(), 0.01);
		assertEquals(0.0f, labColorWhite.getB(), 0.01);
	}

	// Convert RGB to XYZ color space accurately
	@Test
	public void test_rgb_to_xyz_conversion_accuracy() {
		int red = 255;
		int green = 0;
		int blue = 0;

		float[] xyz = ColorConverter.RGBtoXYZ(red, green, blue);
		assertEquals(41.24f, xyz[0], 0.5);
		assertEquals(21.26f, xyz[1], 0.5);
		assertEquals(1.93f, xyz[2], 0.5);
	}

	// Convert XYZ to RGB color space accurately
	@Test
	public void test_xyz_to_rgb_conversion_accuracy() {
		float x = 41.24f;
		float y = 21.26f;
		float z = 1.93f;

		int[] rgb = ColorConverter.XYZtoRGB(x, y, z);
		assertEquals(254, rgb[0]);
		assertEquals(0, rgb[1]);
		assertEquals(0, rgb[2]);
	}

	// Convert LAB to XYZ color space accurately
	@Test
	public void test_lab_to_xyz_conversion_accuracy() {
		float l = 53.23288f;
		float a = 80.10933f;
		float b = 67.22006f;
		float[] tristimulus = {95.047f, 100.000f, 108.883f}; // D65 standard tristimulus

		float[] xyz = ColorConverter.LABtoXYZ(l, a, b, tristimulus);
		assertEquals(41.24f, xyz[0], 0.5);
		assertEquals(21.26f, xyz[1], 0.5);
		assertEquals(1.93f, xyz[2], 0.5);
	}

	// Convert XYZ to LAB color space accurately
	@Test
	public void test_xyz_to_lab_conversion_accuracy() {
		float x = 41.24f;
		float y = 21.26f;
		float z = 1.93f;
		float[] tristimulus = {95.047f, 100.000f, 108.883f}; // D65 standard tristimulus

		float[] lab = ColorConverter.XYZtoLAB(x, y, z, tristimulus);
		assertEquals(53.23288f, lab[0], 0.7);
		assertEquals(80.10933f, lab[1], 0.7);
		assertEquals(67.22006f, lab[2], 0.7);
	}

	// Convert RGB to HEX color code accurately
	@Test
	public void test_rgb_to_hex_conversion_accuracy() {
		int red = 255;
		int green = 0;
		int blue = 0;

		String hex = ColorConverter.RGBtoHEX(red, green, blue);
		assertEquals("FF0000", hex);
	}

	// Handle invalid RGB values in HEX conversion
	@Test
	public void test_rgb_to_hex_invalid_values() {
		assertThrows(IllegalArgumentException.class, () -> {
			ColorConverter.RGBtoHEX(-1, 0, 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			ColorConverter.RGBtoHEX(256, 0, 0);
		});
	}
}
