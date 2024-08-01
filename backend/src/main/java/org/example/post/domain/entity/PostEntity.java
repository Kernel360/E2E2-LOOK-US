package org.example.post.domain.entity;

import java.util.List;

import org.example.common.TimeTrackableEntity;
import org.example.post.domain.dto.PostCreateRequestDto;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends TimeTrackableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;

	@ManyToOne(fetch = FetchType.EAGER)
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

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<UserPostLikesEntity> likesList;

	@Builder
	public PostEntity(Long postId, UserEntity user, String imageUrl, String postContent, Integer likeCount, List<UserPostLikesEntity> likesList) {
		this.postId = postId;
		this.user = user;
		this.imageUrl = imageUrl;
		this.postContent = postContent;
		this.likeCount = likeCount;
		this.likesList = likesList;
	}
}
