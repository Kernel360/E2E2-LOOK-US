package org.example.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.image.AsyncImageAnalyzePipeline;
import org.example.image.ImageAnalyzeManager.analyzer.repository.ClothAnalyzeDataRepository;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.example.image.redis.service.ImageRedisService;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.CategoryEntity;
import org.example.post.domain.entity.LikeEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.CategoryRepository;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.LikeRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.repository.custom.UpdateScoreType;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.example.user.service.member.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class PostServiceTest {

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private PostService postService;
	private UserService userService;

	private ImageRedisService imageRedisService;
	private PostRepository postRepository;
	private UserRepository userRepository;
	private ImageStorageManager imageStorageManager;
	private AsyncImageAnalyzePipeline asyncImageAnalyzePipeline;
	private LikeRepository likeRepository;
	private HashtagRepository hashtagRepository;
	private ClothAnalyzeDataRepository clothAnalyzeDataRepository;
	private CategoryRepository categoryRepository;

	private UserEntity user;
	private PostEntity post;
	private LikeEntity like;

	// Successfully creates a post with valid user email, post content, and image
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();

		imageRedisService = mock(ImageRedisService.class);
		postRepository = mock(PostRepository.class);
		userRepository = mock(UserRepository.class);
		imageStorageManager = mock(ImageStorageManager.class);
		asyncImageAnalyzePipeline = mock(AsyncImageAnalyzePipeline.class);
		likeRepository = mock(LikeRepository.class);
		hashtagRepository = mock(HashtagRepository.class);
		clothAnalyzeDataRepository = mock(ClothAnalyzeDataRepository.class);
		categoryRepository = mock(CategoryRepository.class);

		user = mock(UserEntity.class); // Mock the user
		// when(user.getUserId()).thenReturn(1L); // Set the expected userId
		// when(user.getUsername()).thenReturn("username");
		// when(user.getEmail()).thenReturn("test@gmail.com"); // Mock additional methods if needed
		// when(user.getRole()).thenReturn(Role.ROLE_USER);

		post = mock(PostEntity.class); // Mock the post
		// when(post.getPostId()).thenReturn(1L); // Set the expected postId
		// when(post.getUser()).thenReturn(user); // Set the user for the post
		// when(post.getPostContent()).thenReturn("post");
		// when(post.getImageLocationId()).thenReturn(1L);
		// when(post.getLikeCount()).thenReturn(0);
		// when(post.getPostStatus()).thenReturn(PostStatus.PUBLISHED);

		like = mock(LikeEntity.class);

		// Security Context 설정
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
			user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		// 실제 PostService 객체를 생성하고 모킹된 의존성을 주입합니다.
		postService = new PostService(
			imageRedisService,
			postRepository,
			userRepository,
			imageStorageManager,
			asyncImageAnalyzePipeline,
			likeRepository,
			hashtagRepository,
			clothAnalyzeDataRepository,
			categoryRepository
		);
	}

	@Test
	public void test_create_post_success() throws IOException {
		// Arrange
		String email = "test@example.com";
		MultipartFile image = mock(MultipartFile.class);
		PostDto.CreatePostDtoRequest postDto = new PostDto.CreatePostDtoRequest("Test content", "#test",
			"category1,category2");

		StorageSaveResult storageSaveResult = new StorageSaveResult(StorageType.LOCAL_FILE_SYSTEM, 1L);
		PostEntity postEntity = new PostEntity(user, "Test content", 1L, 0);

		Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		Mockito.when(imageStorageManager.saveImage(image, StorageType.LOCAL_FILE_SYSTEM)).thenReturn(storageSaveResult);
		Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(postEntity);
		Mockito.when(categoryRepository.findByCategoryContent(Mockito.anyString()))
			.thenReturn(new CategoryEntity("category1"));

		// Act
		PostDto.CreatePostDtoResponse response = postService.createPost(postDto, email, image);

		// Assert
		assertNotNull(response);
		assertEquals(postEntity.getPostId(), response.postId());
	}

	@Test
	public void test_update_post_success() throws IOException {
		// Arrange
		PostDto.CreatePostDtoRequest updateRequest = new PostDto.CreatePostDtoRequest("New content", "#newhashtag",
			"newcategory");
		MultipartFile image = Mockito.mock(MultipartFile.class);
		Mockito.when(image.isEmpty()).thenReturn(true);

		// Mocking behavior of postService
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		Mockito.when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));

		// Mocking hashtag repository behavior
		Mockito.when(hashtagRepository.findAllByPost(post)).thenReturn(new ArrayList<>());
		Mockito.when(hashtagRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<>());

		// Mocking category repository behavior
		Mockito.when(categoryRepository.findAllByCategoryContent(Mockito.anyString()))
			.thenReturn(List.of(new CategoryEntity("newcategory")));

		// Act
		when(post.getPostStatus()).thenReturn(PostStatus.PUBLISHED);
		when(user.getUserId()).thenReturn(1L); // Set the expected userId
		when(post.getUser()).thenReturn(user); // Set the user for the post

		PostDto.CreatePostDtoResponse response = postService.updatePost(updateRequest, image, user.getEmail(),
			post.getPostId());

		// Assert
		assertNotNull(response);
		assertEquals(post.getPostId(), response.postId());
	}

	// Returns a page of posts when valid search conditions and pageable are provided
	@Test
	public void test_returns_page_of_posts_with_valid_conditions() {
		// Arrange

		PostSearchCondition searchCondition = new PostSearchCondition();
		searchCondition.setPostContent("test content");
		Pageable pageable = PageRequest.of(0, 10);
		List<PostDto.PostDtoResponse> posts = List.of(
			new PostDto.PostDtoResponse("user1", 1L, 1L, List.of("tag1"), List.of("cat1"), 10, 100, LocalDateTime.now())
		);
		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(posts, pageable, posts.size());

		when(postRepository.search(searchCondition, pageable)).thenReturn(expectedPage);

		// Act
		Page<PostDto.PostDtoResponse> result = postService.findAllPosts(searchCondition, pageable);

		// Assert
		assertEquals(expectedPage, result);
	}

	// Retrieves a post by its ID successfully
	@Test
	public void test_retrieves_post_by_id_successfully() {
		// Arrange
		Long postId = 1L;

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(post.getPostContent()).thenReturn("Sample content");
		when(post.getPostId()).thenReturn(postId);
		when(post.getUser()).thenReturn(user);

		// Act
		PostDto.PostDetailDtoResponse response = postService.getPostById(postId);

		// Assert
		assertNotNull(response);
		assertEquals(postId, response.postId());
		assertEquals("Sample content", response.postContent());
	}

	// User likes a post successfully
	@Test
	public void user_likes_post_successfully() throws JsonProcessingException {
		// Arrange
		Long postId = 1L;
		String email = "user@example.com";

		// Mock behavior for PostEntity
		when(post.getImageLocationId()).thenReturn(1L);

		// Mock behavior for service methods
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));
		when(postService.existLikePost(user, post)).thenReturn(false); // User hasn't liked the post yet
		when(likeRepository.save(any(LikeEntity.class))).thenReturn(like);

		// Act
		Boolean result = postService.like(postId, email);

		// Assert
		assertTrue(result); // Expecting true because the user is liking the post
		verify(asyncImageAnalyzePipeline).updateScore(1L, UpdateScoreType.LIKE);
		verify(post).increaseLikeCount();
		verify(likeRepository).save(any(LikeEntity.class));
	}

	@Test
	public void user_unlikes_post_successfully() throws JsonProcessingException {
		// Arrange
		Long postId = 1L;
		String email = "user@example.com";

		// Mock behavior for PostEntity
		when(post.getImageLocationId()).thenReturn(1L);

		// Mock behavior for service methods
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(postService.existLikePost(user, post)).thenReturn(true); // User has already liked the post
		when(likeRepository.findByUserAndPost(user, post)).thenReturn(like);

		// Act
		Boolean result = postService.like(postId, email);

		// Assert
		assertFalse(result); // Expecting false because the user is unliking the post
		verify(asyncImageAnalyzePipeline).updateScore(1L, UpdateScoreType.LIKE_CANCEL);
		verify(post).decreaseLikeCount();
		verify(likeRepository).delete(like);
	}


	@Test
	public void increments_view_count_first_time() throws JsonProcessingException {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Cookie[] cookies = new Cookie[0]; // No cookies present initially
		when(request.getCookies()).thenReturn(cookies);
		when(post.getImageLocationId()).thenReturn(1L); // 이미지 ID 설정
		when(postRepository.findById(1L)).thenReturn(Optional.of(post)); // 포스트가 존재한다고 설정

		// Spy를 사용하여 실제 PostService 객체 생성
		postService = spy(new PostService(
			imageRedisService,
			postRepository,
			userRepository,
			imageStorageManager,
			asyncImageAnalyzePipeline,
			likeRepository,
			hashtagRepository,
			clothAnalyzeDataRepository,
			categoryRepository
		));


		// Act
		postService.viewCount(1L, request, response);

		// Assert
		verify(postService, times(1)).updateView(1L); // Verify updateView was called

		// Verify the cookie addition
		ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
		verify(response).addCookie(cookieCaptor.capture());
		Cookie addedCookie = cookieCaptor.getValue();
		assertNotNull(addedCookie);
		assertEquals("postView", addedCookie.getName());
		assertEquals("[1]", addedCookie.getValue());
	}
	@Test
	public void updates_view_count_and_cookie_when_cookie_contains_post_id() throws Exception {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Cookie existingCookie = new Cookie("postView", "[1]"); // Cookie already contains the post ID
		Cookie[] cookies = {existingCookie};
		when(request.getCookies()).thenReturn(cookies);

		// Spy 객체 생성
		PostService postService = spy(new PostService(
			imageRedisService,
			postRepository,
			userRepository,
			imageStorageManager,
			asyncImageAnalyzePipeline,
			likeRepository,
			hashtagRepository,
			clothAnalyzeDataRepository,
			categoryRepository
		));

		// Act
		postService.viewCount(1L, request, response);

		// Assert
		// Verify updateView is not called because the post ID is already in the cookie
		verify(postService, never()).updateView(1L);

		// Verify that no cookie was added or changed
		verify(response, never()).addCookie(any(Cookie.class));
	}


	@Test
	public void updates_view_count_and_cookie_when_cookie_does_not_contain_post_id() throws Exception {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Cookie existingCookie = new Cookie("postView", "[0]"); // Cookie does not contain the post ID
		Cookie[] cookies = {existingCookie};
		when(request.getCookies()).thenReturn(cookies);
		when(postRepository.findById(1L)).thenReturn(Optional.of(post)); // 포스트가 존재한다고 설정

		// Spy 객체 생성
		PostService postService = spy(new PostService(
			imageRedisService,
			postRepository,
			userRepository,
			imageStorageManager,
			asyncImageAnalyzePipeline,
			likeRepository,
			hashtagRepository,
			clothAnalyzeDataRepository,
			categoryRepository
		));

		// Act
		postService.viewCount(1L, request, response);

		// Assert
		// Verify updateView is called because the post ID is not in the cookie
		verify(postService, times(1)).updateView(1L);

		// Verify the cookie is updated
		ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
		verify(response).addCookie(cookieCaptor.capture());
		Cookie addedCookie = cookieCaptor.getValue();
		assertNotNull(addedCookie);
		assertEquals("postView", addedCookie.getName());
		assertEquals("[0]_[1]", addedCookie.getValue()); // Cookie value should be "[0]_[1]"
	}


}