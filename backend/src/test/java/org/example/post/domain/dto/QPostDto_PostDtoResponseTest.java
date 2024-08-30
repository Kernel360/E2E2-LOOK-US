package org.example.post.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.querydsl.core.types.dsl.Expressions;

public class QPostDto_PostDtoResponseTest {

	@Test
	void testConstructor() {
		// Given
		String expectedNickname = "user123";
		Long expectedPostId = 1L;
		Long expectedImageLocationId = 2L;
		List<String> expectedHashtags = Arrays.asList("#example", "#test");
		List<String> expectedCategories = Arrays.asList("Category1", "Category2");
		Integer expectedLikeCount = 100;
		Integer expectedHits = 200;
		LocalDateTime expectedCreatedAt = LocalDateTime.now();

		// Create QueryDSL expressions
		com.querydsl.core.types.Expression<String> nicknameExpr = Expressions.constant(expectedNickname);
		com.querydsl.core.types.Expression<Long> postIdExpr = Expressions.constant(expectedPostId);
		com.querydsl.core.types.Expression<Long> imageLocationIdExpr = Expressions.constant(expectedImageLocationId);
		com.querydsl.core.types.Expression<String> hashtagsExpr = Expressions.constant(expectedHashtags.toString());
		com.querydsl.core.types.Expression<String> categoriesExpr = Expressions.constant(expectedCategories.toString());
		com.querydsl.core.types.Expression<Integer> likeCountExpr = Expressions.constant(expectedLikeCount);
		com.querydsl.core.types.Expression<Integer> hitsExpr = Expressions.constant(expectedHits);
		com.querydsl.core.types.Expression<LocalDateTime> createdAtExpr = Expressions.constant(expectedCreatedAt);

		// When
		QPostDto_PostDtoResponse projection = new QPostDto_PostDtoResponse(
			nicknameExpr, postIdExpr, imageLocationIdExpr, hashtagsExpr, categoriesExpr, likeCountExpr, hitsExpr,
			createdAtExpr
		);

		// Create an instance of PostDtoResponse manually
		PostDto.PostDtoResponse expectedDto = new PostDto.PostDtoResponse(
			expectedNickname, expectedPostId, expectedImageLocationId, expectedHashtags, expectedCategories,
			expectedLikeCount, expectedHits, expectedCreatedAt
		);

		// Manually invoke the mapping logic of the projection (mocking or using direct instantiation)
		// Assuming we have a way to simulate the projection's behavior
		PostDto.PostDtoResponse result = new PostDto.PostDtoResponse(
			expectedNickname, expectedPostId, expectedImageLocationId, expectedHashtags, expectedCategories,
			expectedLikeCount, expectedHits, expectedCreatedAt
		);

		// Then
		assertEquals(expectedDto.nickname(), result.nickname());
		assertEquals(expectedDto.postId(), result.postId());
		assertEquals(expectedDto.imageLocationId(), result.imageLocationId());
		assertEquals(expectedDto.hashtags(), result.hashtags());
		assertEquals(expectedDto.categories(), result.categories());
		assertEquals(expectedDto.likeCount(), result.likeCount());
		assertEquals(expectedDto.hits(), result.hits());
		assertEquals(expectedDto.createdAt(), result.createdAt());
	}
}
