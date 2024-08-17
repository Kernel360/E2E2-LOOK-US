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
		String hashtagContents
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

	// TODO: 원래 ID를 받는게 맞지만, 프론트 테스트를 위해서 Resource를 바로 던지는 것으로 변경될 예정입니다.
	//       이후 프론트 개발이 진행될 때는 image_id를 사용합니다.
	public record PostDetailDtoResponse(
		String nickname,
		Long postId,
		Long imageId,
		String postContent,
		List<String> hashtagContents,
		int likeCount,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {

		@QueryProjection
		public PostDetailDtoResponse(
			String nickname,
			Long postId,
			Long imageId,
			String postContent,
			List<String> hashtagContents,
			int likeCount,
			LocalDateTime createdAt,
			LocalDateTime updatedAt
		) {
			this.nickname = nickname;
			this.postId = postId;
			this.imageId = imageId;
			this.postContent = postContent;
			this.hashtagContents = hashtagContents;
			this.likeCount = likeCount;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}

		public static PostDetailDtoResponse toDto(PostEntity postEntity) {
			return new PostDetailDtoResponse(
				postEntity.getUser().getNickname(),
				postEntity.getPostId(),
				postEntity.getImageId(),
				postEntity.getPostContent(),
				postEntity.getHashtagContents() != null ? postEntity.getHashtagContents() : Collections.emptyList(),
				postEntity.getLikeCount(),
				postEntity.getCreatedAt(),
				postEntity.getUpdatedAt()
			);
		}
	}

	public record PostDtoResponse(
		String nickname,
		Long postId,
		Long imageId,
		List<String> hashtags,
		int likeCount
	) {

		// Canonical Constructor
		@QueryProjection
		public PostDtoResponse(
			String nickname,
			Long postId,
			Long imageId,
			String hashtagContent,
			int likeCount
		) {
			this(nickname, postId, imageId, splitHashtags(hashtagContent), likeCount);
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
		LocalDateTime updatedAt
	) {

		public static PostMyPageDtoResponse toDto(PostEntity postEntity) {
			return new PostMyPageDtoResponse(
				postEntity.getPostId(),
				postEntity.getImageId(),
				postEntity.getPostContent(),
				postEntity.getHashtagContents(),
				postEntity.getLikeCount(),
				postEntity.getCreatedAt(),
				postEntity.getUpdatedAt()
			);
		}
	}
}