package org.example.post.domain.entity;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QPostTotalStatsTest {

	@Test
	void testPostTotalStatsPaths() {
		// Given
		QPostTotalStats qPostTotalStats = QPostTotalStats.postTotalStats;

		// When
		NumberPath<Integer> hitsPath = qPostTotalStats.hits;
		NumberPath<Long> idPath = qPostTotalStats.id;
		NumberPath<Integer> likeCountPath = qPostTotalStats.likeCount;
		DateTimePath<java.time.LocalDateTime> recordedAtPath = qPostTotalStats.recordedAt;
		QPostEntity postPath = qPostTotalStats.post;

		// Then
		assertNotNull(hitsPath, "hits path should not be null");
		assertNotNull(idPath, "id path should not be null");
		assertNotNull(likeCountPath, "likeCount path should not be null");
		assertNotNull(recordedAtPath, "recordedAt path should not be null");
		assertNotNull(postPath, "post path should not be null");

		// Check the path names
		assertEquals("hits", hitsPath.getMetadata().getName(), "Path name for hits is incorrect");
		assertEquals("id", idPath.getMetadata().getName(), "Path name for id is incorrect");
		assertEquals("likeCount", likeCountPath.getMetadata().getName(), "Path name for likeCount is incorrect");
		assertEquals("recordedAt", recordedAtPath.getMetadata().getName(), "Path name for recordedAt is incorrect");
		assertEquals("post", postPath.getMetadata().getName(), "Path name for post is incorrect");
	}

	@Test
	void testPostTotalStatsConstructors() {
		// Given
		QPostTotalStats qPostTotalStatsByVariable = new QPostTotalStats("postTotalStats");
		QPostTotalStats qPostTotalStatsByPath = new QPostTotalStats(QPostTotalStats.postTotalStats);
		QPostTotalStats qPostTotalStatsByMetadata = new QPostTotalStats(QPostTotalStats.postTotalStats.getMetadata());

		// Then
		assertNotNull(qPostTotalStatsByVariable, "QPostTotalStats instance by variable should not be null");
		assertNotNull(qPostTotalStatsByPath, "QPostTotalStats instance by path should not be null");
		assertNotNull(qPostTotalStatsByMetadata, "QPostTotalStats instance by metadata should not be null");
	}
}
