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
@Table(name = "report_post_daily_stats_record")
@Getter
@NoArgsConstructor
public class PostDailyStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stats_total_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity post;

	@Column(name = "today_likes", nullable = false)
	private int todayLikes;

	@Column(name = "today_hits", nullable = false)
	private int todayHits;

	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt;

	@Builder
	public PostDailyStats(PostEntity post, int todayLikes, int todayHits, LocalDateTime recordedAt) {
		this.post = post;
		this.todayLikes = todayLikes;
		this.todayHits = todayHits;
		this.recordedAt = recordedAt;
	}
}
