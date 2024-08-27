package org.example.post.repository;

import java.util.List;

import org.example.post.domain.entity.PostDailyStats;
import org.example.post.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDailyStatsRepository extends JpaRepository<PostDailyStats, Long> {
	List<PostDailyStats> findByPost(PostEntity post);
}

