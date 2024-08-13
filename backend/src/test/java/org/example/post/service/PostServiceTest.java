package org.example.post.service;

import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storage.core.StorageType;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {

	@Mock
	private PostRepository postRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private HashtagRepository hashtagRepository;

	@Mock
	private ImageStorageManager imageStorageManager;

	@InjectMocks
	private PostService postService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createPost_shouldReturnPostDto() {
		// Arrange
		String email = "test@example.com";
		PostDto.CreatePostDtoRequest postDto = new PostDto.CreatePostDtoRequest("content", "#hashtag");
		MultipartFile image = mock(MultipartFile.class);

		UserEntity user = mock(UserEntity.class);
		StorageSaveResult storageSaveResult = new StorageSaveResult(StorageType.LOCAL_FILE_SYSTEM, 1L);
		PostEntity postEntity = new PostEntity(user, "content", 1L, 0);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(imageStorageManager.saveResource(image, StorageType.LOCAL_FILE_SYSTEM)).thenReturn(storageSaveResult);
		when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);

		// Act
		PostDto.CreatePostDtoResponse response = postService.createPost(postDto, email, image);

		// Assert
		assertEquals(postEntity.getPostId(), response.postId());
	}

	@Test
	void createPost_shouldThrowExceptionWhenUserNotFound() {
		// Arrange
		String email = "test@example.com";
		PostDto.CreatePostDtoRequest postDto = new PostDto.CreatePostDtoRequest("content", "#hashtag");
		MultipartFile image = mock(MultipartFile.class);

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> postService.createPost(postDto, email, image));
	}

	@Test
	void findAllPosts_shouldReturnPageOfPosts() {
		// Arrange
		PostSearchCondition postSearchCondition = new PostSearchCondition();
		Pageable pageable = PageRequest.of(0, 10);
		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(Collections.emptyList());

		when(postRepository.search(postSearchCondition, pageable)).thenReturn(expectedPage);

		// Act
		Page<PostDto.PostDtoResponse> result = postService.findAllPosts(postSearchCondition, pageable);

		// Assert
		assertEquals(expectedPage, result);
	}

	@Test
	void getPostById_shouldReturnPostDto() {
		// Arrange
		Long postId = 1L;
		UserEntity user = mock(UserEntity.class);
		PostEntity postEntity = new PostEntity(user, "content", 1L, 0);

		when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

		// Act
		PostDto.PostDetailDtoResponse result = postService.getPostById(postId);

		// Assert
		assertEquals(postEntity.getPostId(), result.postId());
	}

	@Test
	void getPostById_shouldThrowExceptionWhenPostNotFound() {
		// Arrange
		Long postId = 1L;

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> postService.getPostById(postId));
	}
}
