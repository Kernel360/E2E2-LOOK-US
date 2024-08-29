package org.example.image.ImageAnalyzeManager.analyzer.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.image.ImageAnalyzeManager.analyzer.tools.LabColorConverter;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class LabColorTest {
	// Correct conversion of LabColor to JSON string
	@Test
	public void test_labcolor_to_json_conversion() {
		LabColor labColor = new LabColor(50.0f, 25.0f, -25.0f);
		LabColorConverter converter = new LabColorConverter();
		String json = converter.convertToDatabaseColumn(labColor);
		assertEquals("{\"l\":50.0,\"a\":25.0,\"b\":-25.0}", json);
	}

	// Handling of null values for l, a, and b
	@Test
	public void test_labcolor_null_values() {
		LabColor labColor = new LabColor(null, null, null);
		LabColorConverter converter = new LabColorConverter();
		String json = converter.convertToDatabaseColumn(labColor);
		assertEquals("{\"l\":null,\"a\":null,\"b\":null}", json);
	}

	// Handling JsonProcessingException in convertToDatabaseColumn
	@Test
	public void test_convertToDatabaseColumn_throws_JsonProcessingException() throws JsonProcessingException {
		// Mock ObjectMapper
		ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
		when(mockObjectMapper.writeValueAsString(any(LabColor.class)))
			.thenThrow(JsonProcessingException.class);

		LabColorConverter converter = new LabColorConverter();
		// Replace objectMapper with mock
		// Using reflection to set the objectMapper field
		ReflectionTestUtils.setField(converter, "objectMapper", mockObjectMapper);

		LabColor labColor = new LabColor(50.0f, 25.0f, -25.0f);

		// Verify that the exception is thrown
		assertThrows(IllegalArgumentException.class, () -> {
			converter.convertToDatabaseColumn(labColor);
		});
	}

	// Handling JsonProcessingException in convertToEntityAttribute
	@Test
	public void test_convertToEntityAttribute_throws_JsonProcessingException() throws JsonProcessingException {
		// Mock ObjectMapper
		ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
		when(mockObjectMapper.readValue(anyString(), eq(LabColor.class)))
			.thenThrow(JsonProcessingException.class);

		LabColorConverter converter = new LabColorConverter();
		// Replace objectMapper with mock
		// Using reflection to set the objectMapper field
		ReflectionTestUtils.setField(converter, "objectMapper", mockObjectMapper);

		String json = "{\"l\":50.0,\"a\":25.0,\"b\":-25.0}";

		// Verify that the exception is thrown
		assertThrows(IllegalArgumentException.class, () -> {
			converter.convertToEntityAttribute(json);
		});
	}




}