package org.example.post.repository;

import java.util.List;

import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

	Page<PostEntity> findAllByPostStatus(PostStatus postStatus, Pageable pageable);

	@Query("SELECT p FROM PostEntity p LEFT JOIN FETCH p.hashtags h WHERE p.postContent LIKE %:postContent% AND p.postStatus = :postStatus")
	Page<PostEntity> findAllByPostContentContainingAndPostStatus(
		@Param("postContent") String postContent,
		@Param("postStatus") PostStatus postStatus,
		Pageable pageable
	);

	@Query("SELECT p FROM PostEntity p JOIN p.hashtags h WHERE h.hashtagContent IN :hashtagList AND p.postStatus = :postStatus")
	Page<PostEntity> findAllByHashtagsContainingAndPostStatus(
		@Param("hashtagList") List<String> hashtagList,
		@Param("postStatus") PostStatus postStatus,
		Pageable pageable
	);

}