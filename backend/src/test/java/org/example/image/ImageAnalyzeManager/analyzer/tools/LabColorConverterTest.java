package org.example.image.ImageAnalyzeManager.analyzer.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.junit.jupiter.api.Test;

class LabColorConverterTest {
	// Convert LabColor object to JSON string successfully
	@Test
	public void convert_labcolor_to_json_successfully() {
		LabColor labColor = new LabColor(50.0f, 25.0f, -25.0f);
		LabColorConverter converter = new LabColorConverter();
		String json = converter.convertToDatabaseColumn(labColor);
		assertNotNull(json);
		assertTrue(json.contains("\"l\":50.0"));
		assertTrue(json.contains("\"a\":25.0"));
		assertTrue(json.contains("\"b\":-25.0"));
	}
}