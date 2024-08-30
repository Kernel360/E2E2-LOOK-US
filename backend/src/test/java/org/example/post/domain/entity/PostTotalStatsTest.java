package org.example.post.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostTotalStatsTest {

	@Test
	void testPostTotalStatsCreationWithBuilder() {
		// Arrange
		PostEntity post = new PostEntity(); // Replace with proper instantiation if needed
		int likeCount = 100;
		int hits = 200;
		LocalDateTime recordedAt = LocalDateTime.now();

		// Act
		PostTotalStats postTotalStats = PostTotalStats.builder()
			.post(post)
			.likeCount(likeCount)
			.hits(hits)
			.recordedAt(recordedAt)
			.build();

		// Assert
		assertThat(postTotalStats.getPost()).isEqualTo(post);
		assertThat(postTotalStats.getLikeCount()).isEqualTo(likeCount);
		assertThat(postTotalStats.getHits()).isEqualTo(hits);
		assertThat(postTotalStats.getRecordedAt()).isEqualTo(recordedAt);
	}

	@Test
	void testNoArgsConstructor() {
		// Act
		PostTotalStats postTotalStats = new PostTotalStats();

		// Assert
		assertThat(postTotalStats).isNotNull();
		assertThat(postTotalStats.getPost()).isNull();
		assertThat(postTotalStats.getLikeCount()).isEqualTo(0);
		assertThat(postTotalStats.getHits()).isEqualTo(0);
		assertThat(postTotalStats.getRecordedAt()).isNull();
	}
}
