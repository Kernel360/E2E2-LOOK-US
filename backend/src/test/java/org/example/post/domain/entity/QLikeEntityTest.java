package org.example.post.domain.entity;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QLikeEntityTest {

	@Test
	void testLikeEntityPaths() {
		// Given
		QLikeEntity qLikeEntity = QLikeEntity.likeEntity;

		// When
		NumberPath<Long> idPath = qLikeEntity.id;
		DateTimePath<java.time.LocalDateTime> createdAtPath = qLikeEntity.createdAt;
		DateTimePath<java.time.LocalDateTime> updatedAtPath = qLikeEntity.updatedAt;
		DateTimePath<java.time.LocalDateTime> removedAtPath = qLikeEntity.removedAt;
		QPostEntity postPath = qLikeEntity.post;
		org.example.user.domain.entity.member.QUserEntity userPath = qLikeEntity.user;

		// Then
		assertNotNull(idPath, "id path should not be null");
		assertNotNull(createdAtPath, "createdAt path should not be null");
		assertNotNull(updatedAtPath, "updatedAt path should not be null");
		assertNotNull(removedAtPath, "removedAt path should not be null");
		assertNotNull(postPath, "post path should not be null");
		assertNotNull(userPath, "user path should not be null");

		// Check the path names
		assertEquals("id", idPath.getMetadata().getName(), "Path name for id is incorrect");
		assertEquals("createdAt", createdAtPath.getMetadata().getName(), "Path name for createdAt is incorrect");
		assertEquals("updatedAt", updatedAtPath.getMetadata().getName(), "Path name for updatedAt is incorrect");
		assertEquals("removedAt", removedAtPath.getMetadata().getName(), "Path name for removedAt is incorrect");
		assertEquals("post", postPath.getMetadata().getName(), "Path name for post is incorrect");
		assertEquals("user", userPath.getMetadata().getName(), "Path name for user is incorrect");
	}

	@Test
	void testLikeEntityConstructors() {
		// Given
		QLikeEntity qLikeEntityByVariable = new QLikeEntity("likeEntity");
		QLikeEntity qLikeEntityByPath = new QLikeEntity(QLikeEntity.likeEntity);
		QLikeEntity qLikeEntityByMetadata = new QLikeEntity(QLikeEntity.likeEntity.getMetadata());

		// Then
		assertNotNull(qLikeEntityByVariable, "QLikeEntity instance by variable should not be null");
		assertNotNull(qLikeEntityByPath, "QLikeEntity instance by path should not be null");
		assertNotNull(qLikeEntityByMetadata, "QLikeEntity instance by metadata should not be null");
	}
}
