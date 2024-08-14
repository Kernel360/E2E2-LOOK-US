package org.example.post.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.common.TimeTrackableEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.user.domain.entity.member.UserEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET removed_at = CURRENT_TIMESTAMP WHERE post_id=?")
@Where(clause = "removed_at IS NULL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends TimeTrackableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(nullable = false)
	private Long imageId;

	@Column(name = "post_content", nullable = true, columnDefinition = "VARCHAR(255)")
	private String postContent;

	@Column(name = "post_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PostStatus postStatus = PostStatus.PUBLISHED;

	@Column(name = "like_count", nullable = false)
	private int likeCount = 0;

	// @Column(name = "like_count", nullable = false, columnDefinition = "INT")
	// @ColumnDefault("0")
	// private Integer likeCount = 0;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<HashtagEntity> hashtags = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LikeEntity> likes = new ArrayList<>();

	@Column(name = "removed_at")
	private LocalDateTime removedAt;

	@Builder
	public PostEntity(UserEntity user, String postContent, Long imageId, int likeCount) {
		this.user = user;
		this.postContent = postContent;
		this.imageId = imageId;
		this.likeCount = likeCount;
	}

	public void addHashtags(List<HashtagEntity> hashtags) {
		this.hashtags.addAll(hashtags);
	}

	// convert List<HashtagEntity> to List<String>
	public List<String> getHashtagContents() {
		return hashtags.stream()
			.map(HashtagEntity::getHashtagContent)
			.collect(Collectors.toList());
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
		if (likeCount < 0) {
			this.likeCount++;
			throw new IllegalArgumentException("좋아요 수는 음수가 안됩니다");
		}
	}
}
