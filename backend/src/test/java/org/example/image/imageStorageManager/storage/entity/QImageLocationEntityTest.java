package org.example.image.imageStorageManager.storage.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QImageLocationEntityTest {
	// Correct instantiation of QImageLocationEntity with a variable
	@Test
	public void test_instantiation_with_variable() {
		String variable = "imageLocationEntity";
		QImageLocationEntity qImageLocationEntity = new QImageLocationEntity(variable);
		assertNotNull(qImageLocationEntity);
		assertEquals(variable, qImageLocationEntity.getMetadata().getName());
	}

}