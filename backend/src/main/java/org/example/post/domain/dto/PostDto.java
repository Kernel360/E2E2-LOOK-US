package org.example.post.domain.dto;

import java.util.List;

import org.example.post.domain.entity.PostEntity;
import org.springframework.web.multipart.MultipartFile;

public class PostDto {
	public record CreatePostDtoRequest(
		String postContent,
		List<String> hashtagContents,
		MultipartFile image
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
