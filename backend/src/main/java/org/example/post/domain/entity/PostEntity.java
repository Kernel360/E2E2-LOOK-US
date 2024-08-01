package org.example.post.domain.entity;

import org.example.common.TimeTrackableEntity;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends TimeTrackableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long postId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(name = "post_content", columnDefinition = "VARCHAR(255)")
	private String postContent;

	// @Column(name = "post_status")
	// @ColumnDefault("0")
	// private PostStatus postStatus = PostStatus.PUBLISHED;

	@Column(name = "like_count", columnDefinition = "INT")
	@ColumnDefault("0")
	private Integer likeCount = 0;

	// @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	// private List<UserPostLikesEntity> likesList;
}
