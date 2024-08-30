package org.example.post.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

	@Mock
	private PostService postService;

	@InjectMocks
	private PostService postServiceMock;

	@Mock
	private UserService userService;

	@Mock
	private ImageRedisService imageRedisService;

	@InjectMocks
	private ImageRedisService imageRedisServiceMock;
	@Mock
	private ClothAnalyzeDataRepository clothAnalyzeDataRepository;

	private PostRepository postRepository;
	private UserRepository userRepository;
	private ImageStorageManager imageStorageManager;
	private AsyncImageAnalyzePipeline asyncImageAnalyzePipeline;
	private LikeRepository likeRepository;
	private HashtagRepository hashtagRepository;
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

	@Test
	public void test_delete_post_successfully() {
		// Arrange
		Long postId = 1L;
		String email = "user@example.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(post.getUser()).thenReturn(user);

		// Act
		postService.delete(postId, email);

		// Assert
		verify(postRepository, times(1)).delete(post); // delete 메소드가 호출되었는지 확인
	}

	@Test
	public void test_delete_post_fails_for_unauthorized_user() {
		// Arrange
		Long postId = 1L;
		String email = "user@example.com";
		UserEntity anotherUser = mock(UserEntity.class); // 다른 사용자

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(user.getUserId()).thenReturn(1L);
		when(anotherUser.getUserId()).thenReturn(2L);
		when(post.getUser()).thenReturn(anotherUser); // 다른 사용자가 포스트의 소유자임

		// Act & Assert
		ApiPostException exception = assertThrows(ApiPostException.class, () -> {
			postService.delete(postId, email);
		});

		// 예외의 카테고리와 서브카테고리가 올바른지 확인
		assertEquals(ApiErrorCategory.RESOURCE_BAD_REQUEST, exception.getErrorCategory());
		assertEquals(ApiPostErrorSubCategory.POST_INVALID_AUTHOR, exception.getErrorSubCategory());

		// postRepository.delete 메서드가 호출되지 않았는지 확인
		verify(postRepository, never()).delete(any(PostEntity.class));

	}

	@Test
	public void test_like_count_success() {
		// Arrange
		Long postId = 1L;
		int expectedLikeCount = 5; // 예상되는 좋아요 수
		PostEntity post = mock(PostEntity.class); // 포스트 엔티티를 모킹

		when(postRepository.findById(postId)).thenReturn(Optional.of(post)); // 포스트가 존재한다고 설정
		when(likeRepository.likeCount(post)).thenReturn(expectedLikeCount); // likeRepository에서 좋아요 수 반환

		// Act
		int actualLikeCount = postService.likeCount(postId);

		// Assert
		assertEquals(expectedLikeCount, actualLikeCount); // 예상되는 좋아요 수와 실제 결과를 비교
	}

	@Test
	public void test_like_count_post_not_found() {
		// Arrange
		Long postId = 1L;

		when(postRepository.findById(postId)).thenReturn(Optional.empty()); // 포스트가 존재하지 않는다고 설정

		// Act & Assert

		assertThrows(ApiPostException.class, () -> postService.likeCount(postId)); // EntityNotFoundException이 발생하는지 확인
	}

	@Test
	public void test_find_post_by_id_not_found() {
		// Arrange
		Long postId = 1L;

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		// Act & Assert
		ApiPostException exception = assertThrows(ApiPostException.class, () -> {
			postService.findPostById(postId);
		});

		// 예외의 카테고리와 서브카테고리가 올바른지 확인
		assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
		assertEquals(ApiPostErrorSubCategory.POST_NOT_FOUND, exception.getErrorSubCategory());

		// postRepository.delete 메서드가 호출되지 않았는지 확인
		assertFalse(exception.getErrorData().equals("잘못된 게시글 조회 요청입니다."));

	}

	@Test
	public void test_find_user_by_email_not_found() {
		// Arrange
		String email = "test@email.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Act & Assert
		ApiUserException exception = assertThrows(ApiUserException.class, () -> {
			postService.findUserByEmail(email);
		});

		// 예외의 카테고리와 서브카테고리가 올바른지 확인
		assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
		assertEquals(ApiUserErrorSubCategory.USER_NOT_FOUND, exception.getErrorSubCategory());

		// postRepository.delete 메서드가 호출되지 않았는지 확인
		assertFalse(exception.getErrorData().equals("존재하는 사용자가 없습니다" + email));

	}

	/*

		@Test
		public void test_findAllPostsByRGB_success() throws JsonProcessingException {
			// Arrange
			int[] rgbColor = {255, 0, 0}; // RGB 색상 (예: 빨간색)
			Pageable pageable = PageRequest.of(0, 10, Sort.by("postContent")); // 페이지네이션과 정렬 설정

			Set<Long> imageIdSet = Set.of(2L, 1L); // RGB와 유사한 이미지 ID 목록
			List<PostDto.PostDtoResponse> postDtoResponses = List.of(
				new PostDto.PostDtoResponse("user1", 1L, 1L, List.of("tag1"), List.of("cat1"), 10, 100, LocalDateTime.now()),
				new PostDto.PostDtoResponse("user2", 2L, 2L, List.of("tag2"), List.of("cat2"), 20, 200, LocalDateTime.now())
			);

			List<int[]> similarColorList = List.of(new int[] {250, 0, 0}, new int[] {255, 10, 0}); // 유사 색상 리스트

			// Mocking 설정
			when(postServiceMock.findImageIdsByRGBAndSimilarColors(rgbColor)).thenReturn(imageIdSet);
			when(postServiceMock.findPostsByImageIds(imageIdSet)).thenReturn(postDtoResponses);
			when(imageRedisServiceMock.getCloseColorList(eq(rgbColor), anyInt())).thenReturn(similarColorList);

			// Act
			Page<PostDto.PostDtoResponse> result = postService.findAllPostsByRGB(rgbColor, pageable);

			// Assert
			assertNotNull(result);
			assertEquals(postDtoResponses.size(), result.getTotalElements());
			assertEquals(postDtoResponses.get(0).postId(), result.getContent().get(0).postId());
		}
	*/
	@Test
	public void test_get_all_category() {
		// Arrange
		List<CategoryEntity> expectedCategories = new ArrayList<>(); // Populate with test data if necessary

		// Mocking
		when(categoryRepository.findAll()).thenReturn(expectedCategories);
		when(postService.getAllCategory()).thenReturn(expectedCategories);

		// Act
		List<CategoryEntity> actualCategories = postService.getAllCategory();

		// Assert
		assertNotNull(actualCategories);
		assertEquals(expectedCategories.size(), actualCategories.size()); // Adjust assertions as needed
	}

	@Test
	public void test_findAllPostsByCategory_success() {
		// Arrange
		Long categoryId = 1L;
		String category = "category1";
		Pageable pageable = PageRequest.of(0, 10);

		List<PostEntity> posts = List.of(
			mock(PostEntity.class),
			mock(PostEntity.class)
		);
		when(posts.get(0).getPostId()).thenReturn(1L);
		when(posts.get(0).getUser()).thenReturn(mock(UserEntity.class));
		when(posts.get(0).getUser().getNickname()).thenReturn("nick1");
		when(posts.get(0).getCategoryContents()).thenReturn(List.of("category1"));

		when(posts.get(1).getPostId()).thenReturn(2L);
		when(posts.get(1).getUser()).thenReturn(mock(UserEntity.class));
		when(posts.get(1).getUser().getNickname()).thenReturn("nick2");
		when(posts.get(1).getCategoryContents()).thenReturn(List.of("category1"));

		List<PostDto.PostDtoResponse> postDtoResponses = posts.stream()
			.map(postEntity -> new PostDto.PostDtoResponse(
				postEntity.getUser().getNickname(),
				postEntity.getPostId(),
				postEntity.getImageLocationId(),
				postEntity.getHashtagContents(),
				postEntity.getCategoryContents(),
				postEntity.getLikeCount(),
				postEntity.getHits(),
				postEntity.getCreatedAt()))
			.toList();

		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(postDtoResponses, pageable,
			postDtoResponses.size());

		when(postRepository.findAllByCategoryContent(category)).thenReturn(posts);

		// Act
		Page<PostDto.PostDtoResponse> result = postService.findAllPostsByCategory(category, pageable);

		// Assert
		assertNotNull(result);
		assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
		assertEquals(expectedPage.getContent(), result.getContent());
		verify(postRepository).findAllByCategoryContent(category);
	}
	@Test
	public void test_findPostsByImageIds_success() {
		// Arrange
		Set<Long> imageIdSet = Set.of(1L, 2L);

		// Mock UserEntity
		UserEntity userEntity1 = mock(UserEntity.class);
		when(userEntity1.getNickname()).thenReturn("user1");

		UserEntity userEntity2 = mock(UserEntity.class);
		when(userEntity2.getNickname()).thenReturn("user2");

		// Mock PostEntity
		PostEntity postEntity1 = mock(PostEntity.class);
		when(postEntity1.getUser()).thenReturn(userEntity1);
		when(postEntity1.getPostId()).thenReturn(1L);
		when(postEntity1.getImageLocationId()).thenReturn(1L);
		when(postEntity1.getHashtagContents()).thenReturn(List.of("tag1"));
		when(postEntity1.getCategoryContents()).thenReturn(List.of("cat1"));
		when(postEntity1.getLikeCount()).thenReturn(10);
		when(postEntity1.getHits()).thenReturn(100);
		when(postEntity1.getCreatedAt()).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0, 0));

		PostEntity postEntity2 = mock(PostEntity.class);
		when(postEntity2.getUser()).thenReturn(userEntity2);
		when(postEntity2.getPostId()).thenReturn(2L);
		when(postEntity2.getImageLocationId()).thenReturn(2L);
		when(postEntity2.getHashtagContents()).thenReturn(List.of("tag2"));
		when(postEntity2.getCategoryContents()).thenReturn(List.of("cat2"));
		when(postEntity2.getLikeCount()).thenReturn(20);
		when(postEntity2.getHits()).thenReturn(200);
		when(postEntity2.getCreatedAt()).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0, 0));

		// Set up repository mocks
		when(postRepository.findAllByImageLocationId(1L)).thenReturn(List.of(postEntity1));
		when(postRepository.findAllByImageLocationId(2L)).thenReturn(List.of(postEntity2));

		// Expected result setup
		List<PostDto.PostDtoResponse> expectedPostDtoResponses = new ArrayList<>();
		expectedPostDtoResponses.add(new PostDto.PostDtoResponse("user1", 1L, 1L, List.of("tag1"), List.of("cat1"),
			10, 100, LocalDateTime.of(2024, 1, 1, 0, 0, 0)));
		expectedPostDtoResponses.add(new PostDto.PostDtoResponse("user2", 2L, 2L, List.of("tag2"), List.of("cat2"),
			20, 200, LocalDateTime.of(2024, 1, 1, 0, 0, 0)));

		// Act
		List<PostDto.PostDtoResponse> result = postService.findPostsByImageIds(imageIdSet);

		// Assert
		assertNotNull(result);
		assertThat(result).containsExactlyInAnyOrderElementsOf(expectedPostDtoResponses);

		verify(postRepository).findAllByImageLocationId(2L);
		verify(postRepository).findAllByImageLocationId(1L);
	}
}