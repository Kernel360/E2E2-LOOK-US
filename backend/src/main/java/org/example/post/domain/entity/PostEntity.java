package org.example.post.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.common.TimeTrackableEntity;
import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.post.domain.enums.PostStatus;
import org.example.user.domain.entity.member.UserEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET removed_at = CURRENT_TIMESTAMP WHERE post_id=?")
@SQLRestriction("removed_at IS NULL")
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
	private Long imageLocationId;

	@Column(name = "post_content", columnDefinition = "VARCHAR(255)")
	private String postContent;

	@Column(name = "post_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PostStatus postStatus = PostStatus.PUBLISHED;

	@Column(name = "like_count", nullable = false)
	private int likeCount = 0;

	@Column(columnDefinition = "integer default 0", nullable = false)
	private int hits;

	@Column(name = "today_likes", nullable = false)
	private int todayLikes;  // 오늘의 좋아요 수

	@Column(name = "today_hits", nullable = false)
	private int todayHits;   // 오늘의 조회수

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<HashtagEntity> hashtags = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LikeEntity> likes = new ArrayList<>();

	@Column(name = "removed_at")
	private LocalDateTime removedAt;

	@Builder
	public PostEntity(UserEntity user, String postContent, Long imageLocationId, int likeCount) {
		this.user = user;
		this.postContent = postContent;
		this.imageLocationId = imageLocationId;
		this.likeCount = likeCount;
	}

	public void addHashtags(List<HashtagEntity> hashtags) {
		this.hashtags.addAll(hashtags);
	}

	public void updatePostContent(String postContent) {
		this.postContent = postContent;
	}

	public void updateImage(Long imageLocationId) {
		this.imageLocationId = imageLocationId;
	}

	public void updateHashtags(List<HashtagEntity> hashtags) {
		this.hashtags.clear();
		this.hashtags.addAll(hashtags);
	}

	public List<String> getHashtagContents() {
		return hashtags.stream()
			.map(HashtagEntity::getHashtagContent)
			.collect(Collectors.toList());
	}

	// 오늘의 통계 초기화
	public void resetTodayStats() {
		this.todayHits = 0;
		this.todayLikes = 0;
	}

	// 좋아요 수 증가
	public void increaseLikeCount() {
		this.likeCount++;
		this.todayLikes++;  // 오늘의 좋아요 수도 증가
	}

	// 좋아요 수 감소
	public void decreaseLikeCount() {
		this.likeCount--;
		this.todayLikes--;
		if (likeCount < 0 || todayLikes < 0) {
			this.likeCount = 0;
			this.todayLikes = 0;
			log.warn("좋아요 감소 시도에서 오류 발생: 게시글 ID={}, 좋아요 수={}, 오늘의 좋아요 수={}", this.postId, this.likeCount, this.todayLikes);
			throw ApiPostException
				.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiPostErrorSubCategory.POST_INVALID_LIKE_REQUEST)
				.build();
		}
	}
}

