package org.example.post.domain.dto.response;

import org.example.post.domain.enums.PostStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
	private Long postId;
	private String postContent;
	private Integer likeCount;

	// private PostStatus postStatus;
	// private List<UserPostLikesEntity> likesEntityList;
}
