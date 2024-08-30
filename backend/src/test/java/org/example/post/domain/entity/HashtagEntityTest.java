package org.example.post.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashtagEntityTest {

	@Test
	void testHashtagEntityCreation() {
		// Arrange
		PostEntity post = new PostEntity(); // Assuming you have a PostEntity constructor or a way to create a PostEntity.
		String content = "#example";

		// Act
		HashtagEntity hashtagEntity = new HashtagEntity(post, content);

		// Assert
		assertThat(hashtagEntity.getPost()).isEqualTo(post);
		assertThat(hashtagEntity.getHashtagContent()).isEqualTo(content);
	}

	@Test
	void testProtectedNoArgsConstructor() {
		// Act
		HashtagEntity hashtagEntity = new HashtagEntity();

		// Assert
		assertThat(hashtagEntity).isNotNull();
		assertThat(hashtagEntity.getPost()).isNull();
		assertThat(hashtagEntity.getHashtagContent()).isNull();
	}
}
