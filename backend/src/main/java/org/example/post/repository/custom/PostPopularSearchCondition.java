package org.example.post.repository.custom;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostPopularSearchCondition {
	private LocalDateTime createdAt;
	private int likeCount;
	// TODO: 조회수 추가 되어야 함
	// private int viewCount;
}
