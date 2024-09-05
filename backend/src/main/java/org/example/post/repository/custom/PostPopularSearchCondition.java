package org.example.post.repository.custom;

import java.time.LocalDateTime;

import org.example.post.domain.entity.PostEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostPopularSearchCondition {
	private LocalDateTime createdAt;
	private int likeCount;
	private int viewCount;

	public PostPopularSearchCondition(PostEntity post) {
		createdAt = post.getCreatedAt();
		likeCount = post.getLikeCount();
		viewCount = post.getHits();
	}
}
