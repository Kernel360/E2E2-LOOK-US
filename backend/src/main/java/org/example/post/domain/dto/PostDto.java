package org.example.post.domain.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.post.domain.entity.CategoryEntity;
import org.example.post.domain.entity.PostEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

public class PostDto {
	public record CreatePostDtoRequest(
		@JsonProperty("post_content")
		String postContent,

		@JsonProperty("hashtag_content")
		String hashtagContents,

		@JsonProperty("category_content")
		String categoryContents
	) {
		// Split and Convert String to List<String>
		public List<String> convertContents(String contents, String regex) {
			return Arrays.stream(contents.split(regex))
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

	// TODO: 원래 ID를 받는게 맞지만, 프론트 테스트를 위해서 Resource를 바로 던지는 것으로 변경될 예정입니다.
	//       이후 프론트 개발이 진행될 때는 image_id를 사용합니다.
	public record PostDetailDtoResponse(
		String nickname,
		Long postId,
		Long imageLocationId,
		String postContent,
		List<String> hashtagContents,
		List<String> categories,
		int likeCount,
		boolean likeStatus,
		int hits,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {

		public static PostDetailDtoResponse toDto(PostEntity postEntity, boolean likeStatus) {

			return new PostDetailDtoResponse(
				postEntity.getUser().getNickname(),
				postEntity.getPostId(),
				postEntity.getImageLocationId(),
				postEntity.getPostContent(),
				postEntity.getHashtagContents() != null ? postEntity.getHashtagContents() : Collections.emptyList(),
				postEntity.getCategories() != null ? postEntity.getCategories().stream().map(CategoryEntity::getCategoryContent).toList() : Collections.emptyList(),
				postEntity.getLikeCount(),
				likeStatus,
				postEntity.getHits(),
				postEntity.getCreatedAt(),
				postEntity.getUpdatedAt()
			);
		}
	}

	public record PostDtoResponse(
		String nickname,
		Long postId,
		Long imageLocationId,
		List<String> hashtags,
		List<String> categories,
		int likeCount,
		int hits,
		LocalDateTime createdAt
	) {

		// Canonical Constructor
		@QueryProjection
		public PostDtoResponse(
			String nickname,
			Long postId,
			Long imageLocationId,
			String hashtagContent,
			String categoryContent,
			int likeCount,
			int hits,
			LocalDateTime createdAt
		) {
			this(nickname, postId, imageLocationId, splitStrings(hashtagContent, "#"), splitStrings(categoryContent, ","), likeCount, hits, createdAt);
		}

		// Helper Method to split hashtagContent into List<String>
		private static List<String> splitStrings(String hashtagContent, String regExp) {
			if (hashtagContent == null || hashtagContent.isEmpty()) {
				return List.of();
			}
			return Stream.of(hashtagContent.split(regExp))
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
		Long imageLocationId,
		String postContent,
		List<String> hashtagContents,
		List<String> categories,
		Integer likeCount,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {

		public static PostMyPageDtoResponse toDto(PostEntity postEntity) {
			return new PostMyPageDtoResponse(
				postEntity.getPostId(),
				postEntity.getImageLocationId(),
				postEntity.getPostContent(),
				postEntity.getHashtagContents(),
				postEntity.getCategories().stream()
					.map(CategoryEntity::getCategoryContent).toList(),
				postEntity.getLikeCount(),
				postEntity.getCreatedAt(),
				postEntity.getUpdatedAt()
			);
		}
	}
}
