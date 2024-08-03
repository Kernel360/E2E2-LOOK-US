package org.example.post.domain.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
	private Long postId;
	private String postContent;
	private List<String> hashtagContents;
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
