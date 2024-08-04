package org.example.post.domain.entity;

import java.util.List;

import org.example.common.TimeTrackableEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.user.domain.entity.member.UserEntity;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends TimeTrackableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(nullable = false)
	private String imageUrl;

	@Column(name = "post_content", nullable = true, columnDefinition = "VARCHAR(255)")
	private String postContent;

	@Column(name = "post_status", nullable = false)
	@ColumnDefault("0")
	private PostStatus postStatus = PostStatus.PUBLISHED; //TODO: build() 에서 제외

	@Column(name = "like_count", nullable = false, columnDefinition = "INT")
	@ColumnDefault("0")
	private Integer likeCount = 0;

	@OneToMany(mappedBy = "postEntity", fetch = FetchType.LAZY)
	private List<HashtagEntity> hashtags;

	@OneToMany(mappedBy = "postEntity", fetch = FetchType.LAZY)
	private List<UserPostLikesEntity> likesList;

	public PostEntity(UserEntity user, String postContent, String imageSrc, Integer likeCount, PostStatus postStatus) {
		this.user = user;
		this.postContent = postContent;
		this.imageUrl = imageSrc;
		this.likeCount = likeCount;
		this.postStatus = postStatus;
	}

	// convert List<HashtagEntity> to List<String>
	public List<String> getHashtagContents() {
		return hashtags.stream()
			.map(HashtagEntity::getHashtagContent)
			.toList();
	}

}
