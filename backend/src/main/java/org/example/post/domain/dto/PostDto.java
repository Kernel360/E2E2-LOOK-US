package org.example.post.domain.dto;

import java.util.List;

import org.example.post.domain.entity.PostEntity;

public class PostDto {
	public record CreatePostDtoRequest(
		String postContent,
		List<String> hashtagContents
	) {
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
}
