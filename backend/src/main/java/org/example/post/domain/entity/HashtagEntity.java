package org.example.post.domain.entity;

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
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "hashtag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hashtag_id")
	private Long hashtagId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostEntity postEntity;

	@Column(name = "hashtag_content", nullable = false)
	private String hashtagContent;

	public HashtagEntity(PostEntity postEntity, String hashtagContent) {
		this.postEntity = postEntity;
		this.hashtagContent = hashtagContent;
	}

}
