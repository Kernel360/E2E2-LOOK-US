package org.example.post.domain.entity;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import org.example.user.domain.entity.member.QUserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QPostEntityTest {

	@Test
	void testPostEntityPaths() {
		// Given
		QPostEntity qPostEntity = QPostEntity.postEntity;

		// When
		ListPath<CategoryEntity, QCategoryEntity> categoriesPath = qPostEntity.categories;
		DateTimePath<java.time.LocalDateTime> createdAtPath = qPostEntity.createdAt;
		ListPath<HashtagEntity, QHashtagEntity> hashtagsPath = qPostEntity.hashtags;
		NumberPath<Integer> hitsPath = qPostEntity.hits;
		NumberPath<Long> imageLocationIdPath = qPostEntity.imageLocationId;
		NumberPath<Integer> likeCountPath = qPostEntity.likeCount;
		ListPath<LikeEntity, QLikeEntity> likesPath = qPostEntity.likes;
		StringPath postContentPath = qPostEntity.postContent;
		NumberPath<Long> postIdPath = qPostEntity.postId;
		EnumPath<org.example.post.domain.enums.PostStatus> postStatusPath = qPostEntity.postStatus;
		DateTimePath<java.time.LocalDateTime> removedAtPath = qPostEntity.removedAt;
		NumberPath<Integer> todayHitsPath = qPostEntity.todayHits;
		NumberPath<Integer> todayLikesPath = qPostEntity.todayLikes;
		DateTimePath<java.time.LocalDateTime> updatedAtPath = qPostEntity.updatedAt;
		QUserEntity userPath = qPostEntity.user;

		// Then
		assertNotNull(categoriesPath, "categories path should not be null");
		assertNotNull(createdAtPath, "createdAt path should not be null");
		assertNotNull(hashtagsPath, "hashtags path should not be null");
		assertNotNull(hitsPath, "hits path should not be null");
		assertNotNull(imageLocationIdPath, "imageLocationId path should not be null");
		assertNotNull(likeCountPath, "likeCount path should not be null");
		assertNotNull(likesPath, "likes path should not be null");
		assertNotNull(postContentPath, "postContent path should not be null");
		assertNotNull(postIdPath, "postId path should not be null");
		assertNotNull(postStatusPath, "postStatus path should not be null");
		assertNotNull(removedAtPath, "removedAt path should not be null");
		assertNotNull(todayHitsPath, "todayHits path should not be null");
		assertNotNull(todayLikesPath, "todayLikes path should not be null");
		assertNotNull(updatedAtPath, "updatedAt path should not be null");
		assertNotNull(userPath, "user path should not be null");

		// Check the path names
		assertEquals("categories", categoriesPath.getMetadata().getName(), "Path name for categories is incorrect");
		assertEquals("createdAt", createdAtPath.getMetadata().getName(), "Path name for createdAt is incorrect");
		assertEquals("hashtags", hashtagsPath.getMetadata().getName(), "Path name for hashtags is incorrect");
		assertEquals("hits", hitsPath.getMetadata().getName(), "Path name for hits is incorrect");
		assertEquals("imageLocationId", imageLocationIdPath.getMetadata().getName(), "Path name for imageLocationId is incorrect");
		assertEquals("likeCount", likeCountPath.getMetadata().getName(), "Path name for likeCount is incorrect");
		assertEquals("likes", likesPath.getMetadata().getName(), "Path name for likes is incorrect");
		assertEquals("postContent", postContentPath.getMetadata().getName(), "Path name for postContent is incorrect");
		assertEquals("postId", postIdPath.getMetadata().getName(), "Path name for postId is incorrect");
		assertEquals("postStatus", postStatusPath.getMetadata().getName(), "Path name for postStatus is incorrect");
		assertEquals("removedAt", removedAtPath.getMetadata().getName(), "Path name for removedAt is incorrect");
		assertEquals("todayHits", todayHitsPath.getMetadata().getName(), "Path name for todayHits is incorrect");
		assertEquals("todayLikes", todayLikesPath.getMetadata().getName(), "Path name for todayLikes is incorrect");
		assertEquals("updatedAt", updatedAtPath.getMetadata().getName(), "Path name for updatedAt is incorrect");
		assertEquals("user", userPath.getMetadata().getName(), "Path name for user is incorrect");
	}

	@Test
	void testPostEntityConstructors() {
		// Given
		QPostEntity qPostEntityByVariable = new QPostEntity("postEntity");
		QPostEntity qPostEntityByPath = new QPostEntity(QPostEntity.postEntity);
		QPostEntity qPostEntityByMetadata = new QPostEntity(QPostEntity.postEntity.getMetadata());

		// Then
		assertNotNull(qPostEntityByVariable, "QPostEntity instance by variable should not be null");
		assertNotNull(qPostEntityByPath, "QPostEntity instance by path should not be null");
		assertNotNull(qPostEntityByMetadata, "QPostEntity instance by metadata should not be null");
	}
}
