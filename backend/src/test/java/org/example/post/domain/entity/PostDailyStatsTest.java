package org.example.post.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostDailyStatsTest {

	@Test
	void testPostDailyStatsCreationWithBuilder() {
		// Arrange
		PostEntity post = new PostEntity(); // Assume a proper instantiation method for PostEntity
		int todayLikes = 50;
		int todayHits = 75;
		LocalDateTime recordedAt = LocalDateTime.now();

		// Act
		PostDailyStats postDailyStats = PostDailyStats.builder()
			.post(post)
			.todayLikes(todayLikes)
			.todayHits(todayHits)
			.recordedAt(recordedAt)
			.build();

		// Assert
		assertThat(postDailyStats.getPost()).isEqualTo(post);
		assertThat(postDailyStats.getTodayLikes()).isEqualTo(todayLikes);
		assertThat(postDailyStats.getTodayHits()).isEqualTo(todayHits);
		assertThat(postDailyStats.getRecordedAt()).isEqualTo(recordedAt);
	}

	@Test
	void testNoArgsConstructor() {
		// Act
		PostDailyStats postDailyStats = new PostDailyStats();

		// Assert
		assertThat(postDailyStats).isNotNull();
		assertThat(postDailyStats.getPost()).isNull();
		assertThat(postDailyStats.getTodayLikes()).isEqualTo(0);
		assertThat(postDailyStats.getTodayHits()).isEqualTo(0);
		assertThat(postDailyStats.getRecordedAt()).isNull();
	}
}
