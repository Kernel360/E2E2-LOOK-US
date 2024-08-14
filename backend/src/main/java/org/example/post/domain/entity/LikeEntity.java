package org.example.post.domain.entity;

import java.time.LocalDateTime;

import org.example.common.TimeTrackableEntity;
import org.example.user.domain.entity.member.UserEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "\"like\"")
@SQLDelete(sql = "UPDATE `like` SET removed_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "removed_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeEntity extends TimeTrackableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private PostEntity post;

	@Column(name = "removed_at")
	private LocalDateTime removedAt;

	public static LikeEntity toEntity(UserEntity user, PostEntity post) {
		return LikeEntity.builder()
			.post(post)
			.user(user)
			.build();
	}
}