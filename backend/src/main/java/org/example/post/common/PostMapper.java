package org.example.post.common;

import org.example.post.domain.dto.response.PostResponseDto;
import org.example.post.domain.entity.PostEntity;

public class PostMapper {
	public static PostResponseDto toDto(PostEntity postEntity) {
		if (postEntity == null) {
			return null;
		}

		PostResponseDto dto = new PostResponseDto();
		dto.setPostId(postEntity.getPostId());
		dto.setPostContent(postEntity.getPostContent());
		dto.setLikeCount(postEntity.getLikeCount());

		// 주석 처리된 필드와 관계는 DTO에 포함되지 않음
		// dto.setPostStatus(postEntity.getPostStatus()); // 필요 시 추가
		// dto.setLikesEntityList(postEntity.getLikesList()); // 필요 시 추가

		return dto;
	}
}
