package org.example.image.ImageAnalyzeManager.analyzer.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RGBColorTest {
	// RGBColor object creation with valid RGB values
	@Test
	public void test_rgbcolor_creation_with_valid_values() {
		RGBColor color = new RGBColor(100, 150, 200);
		assertEquals(100, color.getRed());
		assertEquals(150, color.getGreen());
		assertEquals(200, color.getBlue());
	}
}