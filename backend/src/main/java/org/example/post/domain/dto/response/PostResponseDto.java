package org.example.post.domain.dto.response;

import org.example.common.TimeTrackableDto;
import org.example.post.domain.enums.PostStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
	private Long postId;
	private String postContent;
	private Integer likeCount;


	// private List<UserPostLikesEntity> likesEntityList;
	//
	// @Builder
	// public static PostResponseDto fromEntity(PostEntity post) {
	// 	return PostResponseDto.builder()
	// 		.post(post.getPostId())
	// 		.build();
	// }
}
