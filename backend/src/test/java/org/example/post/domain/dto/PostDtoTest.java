package org.example.post.domain.dto;

import org.example.post.domain.entity.CategoryEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PostDtoTest {

	private PostEntity postEntity;
	private UserEntity user;

	@BeforeEach
	void setUp() {
		user = UserEntity.builder()
			.username("user1")
			.password("password")
			.email("user1@example.com")
			.gender(Gender.GENDER_MAN)
			.birth("1990-01-01")
			.nickname("nick")
			.instaId("insta1")
			.role(Role.ROLE_USER)
			.profileImageLocationId(1L)
			.provider("provider")
			.providerId("providerId")
			.build();

		postEntity = PostEntity.builder()
			.user(user)
			.postContent("Initial content")
			.imageLocationId(1L)
			.likeCount(10)
			.build();
	}

	@Test
	void testCreatePostDtoRequestConversion() {
		PostDto.CreatePostDtoRequest request = new PostDto.CreatePostDtoRequest(
			"Content",
			"tag1,tag2",
			"category1,category2"
		);

		List<String> hashtags = request.convertContents(request.hashtagContents(), ",");
		List<String> categories = request.convertContents(request.categoryContents(), ",");

		assertEquals(List.of("tag1", "tag2"), hashtags, "Hashtags should be split and converted correctly");
		assertEquals(List.of("category1", "category2"), categories, "Categories should be split and converted correctly");
	}

	@Test
	void testCreatePostDtoResponse() {
		PostDto.CreatePostDtoResponse response = PostDto.CreatePostDtoResponse.toDto(postEntity);

		assertNotNull(response, "CreatePostDtoResponse should not be null");
		assertEquals(postEntity.getPostId(), response.postId(), "Post ID should be mapped correctly");
	}

	@Test
	void testPostDetailDtoResponse() {
		boolean likeStatus = true;

		PostDto.PostDetailDtoResponse response = PostDto.PostDetailDtoResponse.toDto(postEntity, likeStatus);

		assertNotNull(response, "PostDetailDtoResponse should not be null");
		assertEquals(user.getNickname(), response.nickname(), "Nickname should be mapped correctly");
		assertEquals(postEntity.getPostId(), response.postId(), "Post ID should be mapped correctly");
		assertEquals(postEntity.getImageLocationId(), response.imageLocationId(), "Image location ID should be mapped correctly");
		assertEquals(postEntity.getPostContent(), response.postContent(), "Post content should be mapped correctly");
		assertEquals(postEntity.getHashtagContents(), response.hashtagContents(), "Hashtag contents should be mapped correctly");
		assertEquals(
			postEntity.getCategories().stream().map(CategoryEntity::getCategoryContent).toList(),
			response.categories(),
			"Categories should be mapped correctly"
		);
		assertEquals(postEntity.getLikeCount(), response.likeCount(), "Like count should be mapped correctly");
		assertEquals(likeStatus, response.likeStatus(), "Like status should be mapped correctly");
		assertEquals(postEntity.getHits(), response.hits(), "Hits should be mapped correctly");
		assertEquals(postEntity.getCreatedAt(), response.createdAt(), "Creation date should be mapped correctly");
		assertEquals(postEntity.getUpdatedAt(), response.updatedAt(), "Update date should be mapped correctly");
	}

	@Test
	void testPostDtoResponse() {
		String hashtagContent = "#tag1#tag2";
		String categoryContent = "category1,category2";

		PostDto.PostDtoResponse response = new PostDto.PostDtoResponse(
			user.getNickname(),
			postEntity.getPostId(),
			postEntity.getImageLocationId(),
			hashtagContent,
			categoryContent,
			postEntity.getLikeCount(),
			postEntity.getHits(),
			postEntity.getCreatedAt()
		);

		assertNotNull(response, "PostDtoResponse should not be null");
		assertEquals(user.getNickname(), response.nickname(), "Nickname should be mapped correctly");
		assertEquals(postEntity.getPostId(), response.postId(), "Post ID should be mapped correctly");
		assertEquals(postEntity.getImageLocationId(), response.imageLocationId(), "Image location ID should be mapped correctly");
		assertEquals(
			List.of("tag1", "tag2"),
			response.hashtags().stream().map(String::trim).collect(Collectors.toList()),
			"Hashtags should be split and mapped correctly"
		);
		assertEquals(
			List.of("category1", "category2"),
			response.categories().stream().map(String::trim).collect(Collectors.toList()),
			"Categories should be split and mapped correctly"
		);
		assertEquals(postEntity.getLikeCount(), response.likeCount(), "Like count should be mapped correctly");
		assertEquals(postEntity.getHits(), response.hits(), "Hits should be mapped correctly");
		assertEquals(postEntity.getCreatedAt(), response.createdAt(), "Creation date should be mapped correctly");
	}

	@Test
	void testPostMyPageDtoResponse() {
		PostDto.PostMyPageDtoResponse response = PostDto.PostMyPageDtoResponse.toDto(postEntity);

		assertNotNull(response, "PostMyPageDtoResponse should not be null");
		assertEquals(postEntity.getPostId(), response.postId(), "Post ID should be mapped correctly");
		assertEquals(postEntity.getImageLocationId(), response.imageLocationId(), "Image location ID should be mapped correctly");
		assertEquals(postEntity.getPostContent(), response.postContent(), "Post content should be mapped correctly");
		assertEquals(postEntity.getHashtagContents(), response.hashtagContents(), "Hashtag contents should be mapped correctly");
		assertEquals(
			postEntity.getCategories().stream().map(CategoryEntity::getCategoryContent).toList(),
			response.categories(),
			"Categories should be mapped correctly"
		);
		assertEquals(postEntity.getLikeCount(), response.likeCount(), "Like count should be mapped correctly");
		assertEquals(postEntity.getCreatedAt(), response.createdAt(), "Creation date should be mapped correctly");
		assertEquals(postEntity.getUpdatedAt(), response.updatedAt(), "Update date should be mapped correctly");
	}
}
