package org.example.post.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.custom.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {

	List<PostEntity> findAllByPostStatusAndCreatedAtAfterAndLikeCountGreaterThanEqualAndHitsGreaterThanEqual(
		PostStatus postStatus,
		LocalDateTime localDateTime,
		int likeCount,
		int hits
	);

	// @Query("SELECT DISTINCT p FROM PostEntity p JOIN p.hashtags h WHERE p.postContent LIKE %:postContent% AND h.hashtagContent IN :hashtags AND p.postStatus = :postStatus")
	Page<PostEntity> findAllByPostContentContainingAndHashtags_HashtagContentInAndPostStatus(
		@Param("postContent") String postContent,
		@Param("hashtags") List<String> hashtagList,
		@Param("postStatus") PostStatus postStatus,
		Pageable pageable
	);

	List<PostEntity> findAllByImageLocationId(Long imageId);

	// 특정 카테고리에 속한 모든 게시글을 조회하는 메서드
	@Query("SELECT p FROM PostEntity p JOIN p.categories c WHERE c.categoryId = :categoryId")
	List<PostEntity> findAllByCategoryId(@Param("categoryId") Long categoryId);
}