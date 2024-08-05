package org.example.post.common;

import org.example.post.domain.dto.response.PostResponseDto;
import org.example.post.domain.entity.PostEntity;

public class PostMapper {

	public static PostResponseDto toDto(PostEntity postEntity) {
		if (postEntity == null) {
			return null;
		}
		return new PostResponseDto(
			postEntity.getUser().getUserId(),
			postEntity.getPostId(),
			postEntity.getPostContent(),
			postEntity.getLikeCount(),
			postEntity.getCreatedAt(),
			postEntity.getUpdatedAt());
	}
}
