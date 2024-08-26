package org.example.post.repository;

import org.example.post.domain.entity.PostStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatsRepository extends JpaRepository<PostStats, Long> {
}
