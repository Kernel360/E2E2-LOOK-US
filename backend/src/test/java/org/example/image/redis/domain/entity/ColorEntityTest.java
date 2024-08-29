package org.example.image.redis.domain.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ColorEntityTest {
	// Creating a ColorEntity with valid name and RGB values
	@Test
	public void test_create_color_entity_with_valid_values() {
		ColorEntity colorEntity = new ColorEntity("Red", 255, 0, 0, null, null, null, null);
		assertEquals("Red", colorEntity.getName());
		assertEquals(255, colorEntity.getR());
		assertEquals(0, colorEntity.getG());
		assertEquals(0, colorEntity.getB());
	}

	// Creating a ColorEntity with null name
	@Test
	public void test_create_color_entity_with_null_name() {
		ColorEntity colorEntity = new ColorEntity(null, 255, 0, 0, null, null, null, null);
		assertNull(colorEntity.getName());
		assertEquals(255, colorEntity.getR());
		assertEquals(0, colorEntity.getG());
		assertEquals(0, colorEntity.getB());
	}
}