package org.example.post.domain.entity;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QCategoryEntityTest {

	@Test
	void testCategoryEntityPaths() {
		// Given
		QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;

		// When
		StringPath categoryContentPath = qCategoryEntity.categoryContent;
		NumberPath<Long> categoryIdPath = qCategoryEntity.categoryId;

		// Then
		assertNotNull(categoryContentPath, "categoryContent path should not be null");
		assertNotNull(categoryIdPath, "categoryId path should not be null");

		// Check the path names
		assertEquals("categoryContent", categoryContentPath.getMetadata().getName(), "Path name for categoryContent is incorrect");
		assertEquals("categoryId", categoryIdPath.getMetadata().getName(), "Path name for categoryId is incorrect");
	}

	@Test
	void testCategoryEntityConstructors() {
		// Given
		QCategoryEntity qCategoryEntityByVariable = new QCategoryEntity("categoryEntity");
		QCategoryEntity qCategoryEntityByPath = new QCategoryEntity(QCategoryEntity.categoryEntity);
		QCategoryEntity qCategoryEntityByMetadata = new QCategoryEntity(QCategoryEntity.categoryEntity.getMetadata());

		// Then
		assertNotNull(qCategoryEntityByVariable, "QCategoryEntity instance should not be null");
		assertNotNull(qCategoryEntityByPath, "QCategoryEntity instance should not be null");
		assertNotNull(qCategoryEntityByMetadata, "QCategoryEntity instance should not be null");
	}
}
