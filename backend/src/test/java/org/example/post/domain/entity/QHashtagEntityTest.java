package org.example.post.domain.entity;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QHashtagEntityTest {

	@Test
	void testHashtagEntityPaths() {
		// Given
		QHashtagEntity qHashtagEntity = QHashtagEntity.hashtagEntity;

		// When
		StringPath hashtagContentPath = qHashtagEntity.hashtagContent;
		NumberPath<Long> hashtagIdPath = qHashtagEntity.hashtagId;
		QPostEntity postPath = qHashtagEntity.post;

		// Then
		assertNotNull(hashtagContentPath, "hashtagContent path should not be null");
		assertNotNull(hashtagIdPath, "hashtagId path should not be null");
		assertNotNull(postPath, "post path should not be null");

		// Check the path names
		assertEquals("hashtagContent", hashtagContentPath.getMetadata().getName(), "Path name for hashtagContent is incorrect");
		assertEquals("hashtagId", hashtagIdPath.getMetadata().getName(), "Path name for hashtagId is incorrect");
		assertEquals("post", postPath.getMetadata().getName(), "Path name for post is incorrect");
	}

	@Test
	void testHashtagEntityConstructors() {
		// Given
		QHashtagEntity qHashtagEntityByVariable = new QHashtagEntity("hashtagEntity");
		QHashtagEntity qHashtagEntityByPath = new QHashtagEntity(QHashtagEntity.hashtagEntity);
		QHashtagEntity qHashtagEntityByMetadata = new QHashtagEntity(QHashtagEntity.hashtagEntity.getMetadata());

		// Then
		assertNotNull(qHashtagEntityByVariable, "QHashtagEntity instance by variable should not be null");
		assertNotNull(qHashtagEntityByPath, "QHashtagEntity instance by path should not be null");
		assertNotNull(qHashtagEntityByMetadata, "QHashtagEntity instance by metadata should not be null");
	}
}
