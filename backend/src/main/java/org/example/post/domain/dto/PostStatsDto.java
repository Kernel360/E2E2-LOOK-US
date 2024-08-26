package org.example.post.domain.dto;

import org.example.post.domain.enums.PostStatus;

public record PostStatsDto(PostStatus postStatus, Long totalHits, Long totalLikes) {
		public PostStatsDto(PostStatus postStatus, Long totalHits, Long totalLikes) {
			this.postStatus = postStatus;
			this.totalHits = totalHits;
			this.totalLikes = totalLikes;
		}
}

