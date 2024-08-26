package org.example.post.repository.custom;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostPopularSearchCondition {
	private LocalDateTime createdAt;
	private int likeCount;
	private int viewCount;
}
