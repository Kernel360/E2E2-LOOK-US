package org.example.post.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hashtag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hashtag_id")
	private Long hashtagId;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "post_id", nullable = false)
	// private PostEntity postEntity;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Getter
	@Column(name = "hashtag_content", nullable = false)
	private String hashtagContent;

	public HashtagEntity(String hashtagContent) {
		this.hashtagContent = hashtagContent;
	}
}
