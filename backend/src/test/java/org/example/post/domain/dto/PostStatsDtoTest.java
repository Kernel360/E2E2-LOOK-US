package org.example.post.domain.dto;

import org.example.post.domain.enums.PostStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostStatsDtoTest {

	@Test
	void testPostStatsDtoConstructorAndGetters() {
		// Given
		PostStatus status = PostStatus.PUBLISHED; // Replace with actual enum value
		Long hits = 100L;
		Long likes = 50L;

		// When
		PostStatsDto dto = new PostStatsDto(status, hits, likes);

		// Then
		assertEquals(status, dto.postStatus());
		assertEquals(hits, dto.totalHits());
		assertEquals(likes, dto.totalLikes());
	}

	@Test
	void testPostStatsDtoEquality() {
		// Given
		PostStatus status1 = PostStatus.PUBLISHED; // Replace with actual enum value
		Long hits1 = 100L;
		Long likes1 = 50L;

		PostStatus status2 = PostStatus.PUBLISHED; // Replace with actual enum value
		Long hits2 = 100L;
		Long likes2 = 50L;

		PostStatsDto dto1 = new PostStatsDto(status1, hits1, likes1);
		PostStatsDto dto2 = new PostStatsDto(status2, hits2, likes2);

		// When & Then
		assertEquals(dto1, dto2);
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	void testPostStatsDtoToString() {
		// Given
		PostStatus status = PostStatus.PUBLISHED; // Replace with actual enum value
		Long hits = 100L;
		Long likes = 50L;

		PostStatsDto dto = new PostStatsDto(status, hits, likes);

		// When
		String expectedString = "PostStatsDto[postStatus=" + status + ", totalHits=" + hits + ", totalLikes=" + likes + "]";

		// Then
		assertEquals(expectedString, dto.toString());
	}
}
