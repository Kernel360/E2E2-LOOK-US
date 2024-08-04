package org.example.post.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.example.common.TimeTrackableDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto extends TimeTrackableDto {
	private Long userId;
	private Long postId;
	private String postContent;
	private List<String> hashtagContents;
	private Integer likeCount;

	// Constructor for Post Creation Response
	public PostResponseDto(Long postId) {
		this.postId = postId;
	}

	// Constructor for Post Info Response
	public PostResponseDto(Long userId, Long postId, String postContent, Integer likeCount, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.userId = userId;
		this.postId = postId;
		this.postContent = postContent;
		this.likeCount = likeCount;
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

}
