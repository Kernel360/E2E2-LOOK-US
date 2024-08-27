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
@Table(name = "report_post_total_stats_record")
@Getter
@NoArgsConstructor
public class PostTotalStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stats_total_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity post;

	@Column(name = "like_count", nullable = false)
	private int likeCount;

	@Column(name = "hits", nullable = false)
	private int hits;

	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt;

	@Builder
	public PostTotalStats(PostEntity post, int likeCount, int hits, LocalDateTime recordedAt) {
		this.post = post;
		this.likeCount = likeCount;
		this.hits = hits;
		this.recordedAt = recordedAt;
	}
}
