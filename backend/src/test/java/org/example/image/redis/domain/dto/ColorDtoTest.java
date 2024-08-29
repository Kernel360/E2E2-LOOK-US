package org.example.image.redis.domain.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ColorDtoTest {
	// Creating ColorSaveDtoRequest with valid RGB values
	@Test
	public void test_create_ColorSaveDtoRequest_with_valid_RGB_values() {
		Integer r = 255;
		Integer g = 100;
		Integer b = 50;
		Integer originR = 0;
		Integer originG = 0;
		Integer originB = 0;

		ColorDto.ColorSaveDtoRequest request = new ColorDto.ColorSaveDtoRequest(r, g, b, originR, originG, originB);

		assertEquals(r, request.r());
		assertEquals(g, request.g());
		assertEquals(b, request.b());
		assertEquals(originR, request.originR());
		assertEquals(originG, request.originG());
		assertEquals(originB, request.originB());
	}
	// Creating ColorSaveDtoRequest with null RGB values
	@Test
	public void test_create_ColorSaveDtoRequest_with_null_RGB_values() {
		Integer r = null;
		Integer g = null;
		Integer b = null;
		Integer originR = 0;
		Integer originG = 0;
		Integer originB = 0;

		ColorDto.ColorSaveDtoRequest request = new ColorDto.ColorSaveDtoRequest(r, g, b, originR, originG, originB);

		assertNull(request.r());
		assertNull(request.g());
		assertNull(request.b());
		assertEquals(originR, request.originR());
		assertEquals(originG, request.originG());
		assertEquals(originB, request.originB());
	}
}