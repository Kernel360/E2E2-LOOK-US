package org.example.image.redis.domain.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ColorInfoResponseTest {
	// Correctly maps JSON properties to Java fields
	@Test
	public void test_correct_mapping_of_json_properties() throws Exception {
		String json = "{\"name\": {\"value\": \"Red\"}}";
		ObjectMapper objectMapper = new ObjectMapper();
		ColorInfoResponse response = objectMapper.readValue(json, ColorInfoResponse.class);
		assertNotNull(response);
		assertNotNull(response.getName());
		assertEquals("Red", response.getName().getValue());
	}
	// JSON input with missing 'name' property
	@Test
	public void test_json_input_with_missing_name_property() throws Exception {
		String json = "{}";
		ObjectMapper objectMapper = new ObjectMapper();
		ColorInfoResponse response = objectMapper.readValue(json, ColorInfoResponse.class);
		assertNull(response.getName());
	}

	// Deserialization of JSON with unknown fields
	@Test
	public void test_deserialization_with_unknown_fields() throws Exception {
		String json = "{\"name\": {\"value\": \"Blue\"}, \"unknownField\": \"unknownValue\"}";
		ObjectMapper objectMapper = new ObjectMapper();
		ColorInfoResponse response = objectMapper.readValue(json, ColorInfoResponse.class);
		assertNotNull(response);
		assertNotNull(response.getName());
		assertEquals("Blue", response.getName().getValue());
	}
}