package org.example.post.common;

import org.example.post.domain.dto.response.PostCreateResponseDto;
import org.example.post.domain.dto.response.PostGetInfoResponseDto;
import org.example.post.domain.entity.PostEntity;

public class PostMapper {
	public static PostCreateResponseDto toDto(PostEntity postEntity) {
		if (postEntity == null) {
			return null;
		}

		PostCreateResponseDto dto = new PostCreateResponseDto();
		dto.setPostId(postEntity.getPostId());

		// 주석 처리된 필드와 관계는 DTO에 포함되지 않음
		// dto.setPostStatus(postEntity.getPostStatus()); // 필요 시 추가
		// dto.setLikesEntityList(postEntity.getLikesList()); // 필요 시 추가

		return dto;
	}

	public static PostGetInfoResponseDto toPostDto(PostEntity postEntity) { //TODO : 리팩토링 필요..
		if (postEntity == null) {
			return null;
		}

		PostGetInfoResponseDto dto = new PostGetInfoResponseDto();
		dto.setUserId(postEntity.getUser().getUserId());
		dto.setPostId(postEntity.getPostId());
		dto.setPostContent(postEntity.getPostContent());
		dto.setPostCreatedAt(postEntity.getCreatedAt());
		dto.setPostUpdatedAt(postEntity.getUpdatedAt());
		dto.setLikeCount(postEntity.getLikeCount());

		// 주석 처리된 필드와 관계는 DTO에 포함되지 않음
		// dto.setPostStatus(postEntity.getPostStatus()); // 필요 시 추가
		// dto.setLikesEntityList(postEntity.getLikesList()); // 필요 시 추가

		return dto;
	}


}
