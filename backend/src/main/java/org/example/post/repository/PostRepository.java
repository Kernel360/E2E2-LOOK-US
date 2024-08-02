package org.example.post.repository;

import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
	Page<PostEntity> findAllByPostContentContainingAndPostStatus(String postContent, PostStatus postStatus, Pageable pageable);
	Page<PostEntity> findAllByPostStatus(PostStatus postStatus, Pageable pageable);
}
