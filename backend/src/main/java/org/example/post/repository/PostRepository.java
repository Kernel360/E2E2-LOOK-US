package org.example.post.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.custom.CategoryAndColorSearchCondition;
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


	//주어진 카테고리와 이미지 id에 해당하는 게시글 조회하는 메소드
	@Query("SELECT p FROM PostEntity p JOIN p.categories c WHERE c.categoryContent = :category AND p.imageLocationId IN :imageIds")
	List<PostEntity> findAllByCategoryAndImageIds(@Param("category") String category, @Param("imageIds") Set<Long> imageIds);


	// 새로운 메서드 추가
	@Query("SELECT p FROM PostEntity p JOIN p.categories c WHERE c.categoryContent = :categoryContent")
	List<PostEntity> findAllByCategoryContent(@Param("categoryContent") String categoryContent);
}