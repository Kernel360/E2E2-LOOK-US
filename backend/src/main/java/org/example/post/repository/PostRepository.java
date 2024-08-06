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

	Page<PostEntity> findAllByPostContentContainingAndPostStatus(
		@Param("postContent") String postContent,
		@Param("postStatus") PostStatus postStatus,
		Pageable pageable
	);

	@Query("SELECT p FROM PostEntity p JOIN p.hashtags h WHERE p.postStatus = :postStatus AND h.hashtagContent IN :hashtags")
	Page<PostEntity> findAllByPostStatusAndHashtags_HashtagContentIn(
		@Param("postStatus") PostStatus postStatus,
		@Param("hashtags") List<String> hashtags,
		Pageable pageable
	);

	// @Query("SELECT p FROM PostEntity p JOIN p.HashtagEntity h WHERE p.postStatus = :postStatus AND h.hashtagContent IN :hashtagContents")
	// Page<PostEntity> findAllByPostStatusAndHashtagEntity_HashtagContentContaining(
	// 	@Param("postStatus") PostStatus postStatus,
	// 	@Param("hashtagContents") List<String> hashtagContents,
	// 	Pageable pageable
	// );
/*

	@Query("SELECT p FROM PostEntity p JOIN p.hashtags h WHERE h.hashtagContent IN :hashtagList AND p.postStatus = :postStatus")
	Page<PostEntity> findAllByHashtagsContainingAndPostStatus(
		@Param("hashtagList") List<HashtagEntity> hashtagList,
		@Param("postStatus") PostStatus postStatus,
		Pageable pageable
	);
*/

	@Query("SELECT DISTINCT p FROM PostEntity p JOIN p.hashtags h WHERE p.postContent LIKE %:postContent% AND h.hashtagContent IN :hashtagList AND p.postStatus = :postStatus")
	Page<PostEntity> findAllByPostContentContainingAndHashtagsContainingAndPostStatus(
		@Param("postContent") String postContent,
		@Param("hashtagList") List<String> hashtagList,
		@Param("postStatus") PostStatus postStatus,
		Pageable pageable
	);

}