package org.example.post.domain.entity;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QPostDailyStatsTest {

	@Test
	void testPostDailyStatsPaths() {
		// Given
		QPostDailyStats qPostDailyStats = QPostDailyStats.postDailyStats;

		// When
		NumberPath<Long> idPath = qPostDailyStats.id;
		DateTimePath<java.time.LocalDateTime> recordedAtPath = qPostDailyStats.recordedAt;
		NumberPath<Integer> todayHitsPath = qPostDailyStats.todayHits;
		NumberPath<Integer> todayLikesPath = qPostDailyStats.todayLikes;
		QPostEntity postPath = qPostDailyStats.post;

		// Then
		assertNotNull(idPath, "id path should not be null");
		assertNotNull(recordedAtPath, "recordedAt path should not be null");
		assertNotNull(todayHitsPath, "todayHits path should not be null");
		assertNotNull(todayLikesPath, "todayLikes path should not be null");
		assertNotNull(postPath, "post path should not be null");

		// Check the path names
		assertEquals("id", idPath.getMetadata().getName(), "Path name for id is incorrect");
		assertEquals("recordedAt", recordedAtPath.getMetadata().getName(), "Path name for recordedAt is incorrect");
		assertEquals("todayHits", todayHitsPath.getMetadata().getName(), "Path name for todayHits is incorrect");
		assertEquals("todayLikes", todayLikesPath.getMetadata().getName(), "Path name for todayLikes is incorrect");
		assertEquals("post", postPath.getMetadata().getName(), "Path name for post is incorrect");
	}

	@Test
	void testPostDailyStatsConstructors() {
		// Given
		QPostDailyStats qPostDailyStatsByVariable = new QPostDailyStats("postDailyStats");
		QPostDailyStats qPostDailyStatsByPath = new QPostDailyStats(QPostDailyStats.postDailyStats);
		QPostDailyStats qPostDailyStatsByMetadata = new QPostDailyStats(QPostDailyStats.postDailyStats.getMetadata());

		// Then
		assertNotNull(qPostDailyStatsByVariable, "QLikeEntity instance by variable should not be null");
		assertNotNull(qPostDailyStatsByPath, "QLikeEntity instance by path should not be null");
		assertNotNull(qPostDailyStatsByMetadata, "QLikeEntity instance by metadata should not be null");
	}
}
