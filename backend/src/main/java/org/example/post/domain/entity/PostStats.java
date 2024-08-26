package org.example.post.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_stats_record")
@Getter
@NoArgsConstructor
public class PostStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stats_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity post;

	@Column(name = "like_count", nullable = false)
	private int likeCount;

	@Column(name = "hits", nullable = false)
	private int hits;

	@Column(name = "today_likes", nullable = false)
	private int todayLikes;  // 오늘의 좋아요 수

	@Column(name = "today_hits", nullable = false)
	private int todayHits;   // 오늘의 조회수

	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt;

	@Builder
	public PostStats(PostEntity post, int likeCount, int hits, int todayLikes, int todayHits, LocalDateTime recordedAt) {
		this.post = post;
		this.likeCount = likeCount;
		this.hits = hits;
		this.todayLikes = todayLikes;
		this.todayHits = todayHits;
		this.recordedAt = recordedAt;
	}
}
