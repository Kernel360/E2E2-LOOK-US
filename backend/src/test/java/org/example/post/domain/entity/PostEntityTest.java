package org.example.post.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PostEntityTest {

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
	void testBuilderInitialization() {
		assertNotNull(postEntity, "PostEntity instance should not be null");
		assertEquals("Initial content", postEntity.getPostContent(), "Post content should be initialized correctly");
		assertEquals(1L, postEntity.getImageLocationId(), "Image location ID should be initialized correctly");
		assertEquals(10, postEntity.getLikeCount(), "Like count should be initialized correctly");
	}

	@Test
	void testUpdatePostContent() {
		postEntity.updatePostContent("Updated content");
		assertEquals("Updated content", postEntity.getPostContent(), "Post content should be updated");
	}

	@Test
	void testUpdateImage() {
		postEntity.updateImage(2L);
		assertEquals(2L, postEntity.getImageLocationId(), "Image location ID should be updated");
	}

	@Test
	void testGetHashtagContents() {
		HashtagEntity hashtag1 = new HashtagEntity(postEntity, "hashtag1");
		HashtagEntity hashtag2 = new HashtagEntity(postEntity, "hashtag2");
		postEntity.updateHashtags(List.of(hashtag1, hashtag2));

		List<String> hashtagContents = postEntity.getHashtagContents();
		assertEquals(2, hashtagContents.size(), "Hashtag contents size should be 2");
		assertTrue(hashtagContents.contains("hashtag1"), "Hashtag1 should be present in the list");
		assertTrue(hashtagContents.contains("hashtag2"), "Hashtag2 should be present in the list");
	}

	@Test
	void testUpdateHashtags() {
		HashtagEntity hashtag1 = new HashtagEntity(postEntity, "hashtag1");
		HashtagEntity hashtag2 = new HashtagEntity(postEntity, "hashtag2");

		postEntity.updateHashtags(List.of(hashtag1, hashtag2));
		assertEquals(2, postEntity.getHashtags().size(), "Hashtags size should be updated");
		assertTrue(postEntity.getHashtags().contains(hashtag1), "Hashtag1 should be present");
		assertTrue(postEntity.getHashtags().contains(hashtag2), "Hashtag2 should be present");
	}


	@Test
	void testAddHashtags() {
		HashtagEntity hashtag1 = new HashtagEntity(postEntity, "hashtag1");
		HashtagEntity hashtag2 = new HashtagEntity(postEntity, "hashtag2");

		postEntity.addHashtags(List.of(hashtag1, hashtag2));
		assertEquals(2, postEntity.getHashtags().size(), "Hashtags size should be 2 after adding");
		assertTrue(postEntity.getHashtags().contains(hashtag1), "Hashtag1 should be present after adding");
		assertTrue(postEntity.getHashtags().contains(hashtag2), "Hashtag2 should be present after adding");
	}

	@Test
	void testGetCategoryContents() {
		CategoryEntity category1 = new CategoryEntity("Category1");
		CategoryEntity category2 = new CategoryEntity("Category2");
		postEntity.updateCategories(List.of(category1, category2));

		List<String> categoryContents = postEntity.getCategoryContents();
		assertEquals(2, categoryContents.size(), "Category contents size should be 2");
		assertTrue(categoryContents.contains("Category1"), "Category1 should be present in the list");
		assertTrue(categoryContents.contains("Category2"), "Category2 should be present in the list");
	}

	@Test
	void testUpdateCategories() {
		CategoryEntity category1 = new CategoryEntity("Category1");
		CategoryEntity category2 = new CategoryEntity("Category2");

		postEntity.updateCategories(List.of(category1, category2));
		assertEquals(2, postEntity.getCategories().size(), "Categories size should be updated");
		assertTrue(postEntity.getCategories().contains(category1), "Category1 should be present");
		assertTrue(postEntity.getCategories().contains(category2), "Category2 should be present");
	}

	@Test
	void testAddCategories() {
		CategoryEntity category1 = new CategoryEntity("Category1");
		CategoryEntity category2 = new CategoryEntity("Category2");

		postEntity.addCategories(List.of(category1, category2));
		assertEquals(2, postEntity.getCategories().size(), "Categories size should be 2 after adding");
		assertTrue(postEntity.getCategories().contains(category1), "Category1 should be present after adding");
		assertTrue(postEntity.getCategories().contains(category2), "Category2 should be present after adding");
	}

	@Test
	void testResetTodayStats() {
		postEntity.resetTodayStats();
		assertEquals(0, postEntity.getTodayHits(), "Today's hits should be reset to 0");
		assertEquals(0, postEntity.getTodayLikes(), "Today's likes should be reset to 0");
	}

	@Test
	void testIncreaseLikeCount() {
		postEntity.increaseLikeCount();
		assertEquals(11, postEntity.getLikeCount(), "Like count should be incremented by 1");
		assertEquals(1, postEntity.getTodayLikes(), "Today's likes should be incremented by 1");
	}

	@Test
	void testDecreaseLikeCount() {
		// Initial state with at least one like
		postEntity = PostEntity.builder()
			.user(user)
			.postContent("Content")
			.imageLocationId(1L)
			.likeCount(1) // Set likeCount to 1 to test decrementing to zero
			.build();

		// Attempt to decrease further to test exception handling
		ApiPostException thrown = assertThrows(ApiPostException.class, () -> {
			postEntity.decreaseLikeCount(); // This should throw an exception
		}, "Expected decreaseLikeCount() to throw an exception but it didn't");

		assertEquals(ApiErrorCategory.RESOURCE_BAD_REQUEST, thrown.getErrorCategory(),
			"Exception category should be RESOURCE_BAD_REQUEST");
		assertEquals(ApiPostErrorSubCategory.POST_INVALID_LIKE_REQUEST, thrown.getErrorSubCategory(),
			"Exception subCategory should be POST_INVALID_LIKE_REQUEST");
	}

	@Test
	void testDecreaseLikeCountWithErrorHandling() {

		// Attempt to decrease below zero
		ApiPostException thrown = assertThrows(ApiPostException.class, () -> postEntity.decreaseLikeCount(),
			"Expected decreaseLikeCount() to throw, but it didn't");

		assertEquals(ApiErrorCategory.RESOURCE_BAD_REQUEST, thrown.getErrorCategory(),
			"Exception category should be RESOURCE_BAD_REQUEST");
		assertEquals(ApiPostErrorSubCategory.POST_INVALID_LIKE_REQUEST, thrown.getErrorSubCategory(),
			"Exception subCategory should be POST_INVALID_LIKE_REQUEST");
	}

}
