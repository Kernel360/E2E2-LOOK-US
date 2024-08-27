package org.example.post.repository;

import java.util.List;

import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostTotalStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTotalStatsRepository extends JpaRepository<PostTotalStats, Long> {
	List<PostTotalStats> findByPost(PostEntity post);
}