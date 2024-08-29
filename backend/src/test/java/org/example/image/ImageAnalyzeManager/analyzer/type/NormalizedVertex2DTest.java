package org.example.image.ImageAnalyzeManager.analyzer.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NormalizedVertex2DTest {
	// Object creation with valid dx and dy values
	@Test
	public void test_creation_with_valid_values() {
		Float dx = 1.0f;
		Float dy = 2.0f;
		NormalizedVertex2D vertex = new NormalizedVertex2D(dx, dy);
		assertEquals(dx, vertex.getDx());
		assertEquals(dy, vertex.getDy());
	}

	// Object creation with null dx and dy values
	@Test
	public void test_creation_with_null_values() {
		NormalizedVertex2D vertex = new NormalizedVertex2D(null, null);
		assertNull(vertex.getDx());
		assertNull(vertex.getDy());
	}
}