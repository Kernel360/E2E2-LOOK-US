package org.example.post.repository;

import java.util.List;

import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
	List<HashtagEntity> findAllByPost(PostEntity post);
}
