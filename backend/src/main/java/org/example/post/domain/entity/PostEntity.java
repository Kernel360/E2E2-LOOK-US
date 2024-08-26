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
	private Long imageId;

	@Column(name = "post_content", columnDefinition = "VARCHAR(255)")
	private String postContent;

	@Column(name = "post_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PostStatus postStatus = PostStatus.PUBLISHED;

	@Column(name = "like_count", nullable = false)
	private int likeCount = 0;

	@Column(columnDefinition = "integer default 0", nullable = false)
	private int hits;

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

	public void updatePostContent(String postContent) {
		this.postContent = postContent;
	}

	public void updateImage(Long imageId) {
		this.imageId = imageId;
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

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
		if (likeCount < 0) {
			this.likeCount = 0;
			throw ApiPostException
				.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiPostErrorSubCategory.POST_INVALID_LIKE_REQUEST)
				.build();
		}
	}
}
