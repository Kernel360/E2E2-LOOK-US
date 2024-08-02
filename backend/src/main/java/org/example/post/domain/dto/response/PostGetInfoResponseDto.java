package org.example.post.domain.dto.response;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostGetInfoResponseDto {

	private Long userId;
	private Long postId;
	private String postContent;
	private LocalDateTime postCreatedAt;
	private LocalDateTime postUpdatedAt;
	private Integer likeCount;


}
