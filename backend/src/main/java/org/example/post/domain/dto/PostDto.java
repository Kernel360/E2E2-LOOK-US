package org.example.post.domain.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.post.domain.entity.PostEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

public class PostDto {

	public record CreatePostDtoRequest(
		@JsonProperty("post_content")
		String postContent,

		@JsonProperty("hashtag_content")
		String hashtagContents,

		@JsonProperty("category_ids")
		List<Long> categories // 카테고리 ID 리스트 추가
	) {
		// Split and Convert String to List<String>
		public List<String> convertHashtagContents(String hashtagContents, String regex) {
			return Arrays.stream(hashtagContents.split(regex))
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
		}
	}

	public record CreatePostDtoResponse(
		Long postId
	) {
		public static CreatePostDtoResponse toDto(PostEntity postEntity) {
			return new CreatePostDtoResponse(
				postEntity.getPostId()
			);
		}
	}

	public record PostDetailDtoResponse(
		String nickname,
		Long postId,
		Long imageId,
		String postContent,
		List<String> hashtagContents,
		int likeCount,
		boolean likeStatus,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		List<Long> categories // 카테고리 리스트 추가
	) {

		@QueryProjection
		public PostDetailDtoResponse(
			String nickname,
			Long postId,
			Long imageId,
			String postContent,
			List<String> hashtagContents,
			int likeCount,
			boolean likeStatus,
			LocalDateTime createdAt,
			LocalDateTime updatedAt,
			List<Long> categories // 카테고리 리스트 추가
		) {
			this.nickname = nickname;
			this.postId = postId;
			this.imageId = imageId;
			this.postContent = postContent;
			this.hashtagContents = hashtagContents;
			this.likeCount = likeCount;
			this.likeStatus = likeStatus;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
			this.categories = categories;
		}

		public static PostDetailDtoResponse toDto(PostEntity postEntity, boolean likeStatus) {

			return new PostDetailDtoResponse(
				postEntity.getUser().getNickname(),
				postEntity.getPostId(),
				postEntity.getImageId(),
				postEntity.getPostContent(),
				postEntity.getHashtagContents() != null ? postEntity.getHashtagContents() : Collections.emptyList(),
				postEntity.getLikeCount(),
				likeStatus,
				postEntity.getCreatedAt(),
				postEntity.getUpdatedAt(),
				postEntity.getCategories() // 카테고리 리스트 매핑
			);
		}
	}

	public record PostDtoResponse(
		String nickname,
		Long postId,
		Long imageId,
		List<String> hashtags,
		int likeCount,
<<<<<<< Updated upstream
		LocalDateTime createdAt
=======
		List<Long> categories // 카테고리 이름 리스트 추가
>>>>>>> Stashed changes
	) {

		// Canonical Constructor
		@QueryProjection
		public PostDtoResponse(
			String nickname,
			Long postId,
			Long imageId,
			String hashtagContent,
			int likeCount,
<<<<<<< Updated upstream
			LocalDateTime createdAt
		) {
			this(nickname, postId, imageId, splitHashtags(hashtagContent), likeCount, createdAt);
=======
			List<Long> categories // 카테고리 이름 리스트 추가
		) {
			this(nickname, postId, imageId, splitHashtags(hashtagContent), likeCount, categories);
>>>>>>> Stashed changes
		}

		// Helper Method to split hashtagContent into List<String>
		private static List<String> splitHashtags(String hashtagContent) {
			if (hashtagContent == null || hashtagContent.isEmpty()) {
				return List.of();
			}
			return Stream.of(hashtagContent.split("#"))
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
		}

	}

	public record PostIdRequest(
		Long postId
	) {
	}

	public record PostMyPageDtoResponse(
		Long postId,
		Long imageId,
		String postContent,
		List<String> hashtagContents,
		Integer likeCount,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		List<Long> categories // 카테고리 이름 리스트 추가
	) {
		public static PostMyPageDtoResponse toDto(PostEntity postEntity) {
			return new PostMyPageDtoResponse(
				postEntity.getPostId(),
				postEntity.getImageId(),
				postEntity.getPostContent(),
				postEntity.getHashtagContents(),
				postEntity.getLikeCount(),
				postEntity.getCreatedAt(),
				postEntity.getUpdatedAt(),
				postEntity.getCategories()
			);
		}
	}
}