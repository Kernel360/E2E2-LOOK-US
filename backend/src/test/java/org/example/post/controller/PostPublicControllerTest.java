package org.example.post.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.image.redis.domain.dto.ColorDto;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.CategoryEntity;
import org.example.post.repository.custom.CategoryAndColorSearchCondition;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.service.PostService;
import org.example.image.redis.service.ImageRedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(PostPublicController.class)
public class PostPublicControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;

	@MockBean
	private ImageRedisService imageRedisService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		// 테스트 전 초기 설정
	}

	// Returns a paginated list of posts when valid search parameters are provided
	@Test
	public void test_search_post_with_empty_parameters() throws JsonProcessingException {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		PostSearchCondition postSearchCondition = new PostSearchCondition();

		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(List.of(), pageable, 0);

		when(postService.findAllPosts(any(PostSearchCondition.class), eq(pageable))).thenReturn(expectedPage);

		PostPublicController controller = new PostPublicController(postService, imageRedisService);

		// Act
		ResponseEntity<Page<PostDto.PostDtoResponse>> response = controller.searchPost(pageable, null, null, null,
			null);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPage, response.getBody());
	}

	// Returns a paginated list of posts when valid search parameters are provided
	@Test
	public void test_search_post_with_valid_parameters() throws JsonProcessingException {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		PostSearchCondition postSearchCondition = new PostSearchCondition();
		postSearchCondition.setPostContent("example content");
		postSearchCondition.setHashtags("example,hashtag");
		postSearchCondition.setRgbColor(new int[] {255, 0, 0});
		postSearchCondition.setCategory("example category");

		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(List.of(new PostDto.PostDtoResponse(
			"nickname", 1L, 1L, List.of("example", "hashtag"), List.of("example category"), 10, 100, LocalDateTime.now()
		)), pageable, 1);

		when(postService.findAllPostsByRGB(any(int[].class), eq(pageable))).thenReturn(expectedPage);

		PostPublicController controller = new PostPublicController(postService, imageRedisService);

		// Act
		ResponseEntity<Page<PostDto.PostDtoResponse>> response = controller.searchPost(pageable, "example content",
			"example,hashtag", new int[] {255, 0, 0}, "example category");

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPage, response.getBody());
	}



	// API returns 200 OK when valid category and RGB color are provided
	@Test
	public void test_valid_category_and_rgb_color() throws JsonProcessingException {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
		CategoryAndColorSearchCondition condition = new CategoryAndColorSearchCondition();
		condition.setCategory("Nature");
		condition.setRgbColor(new int[]{255, 0, 0});

		List<PostDto.PostDtoResponse> postList = List.of(
			new PostDto.PostDtoResponse("user1", 1L, 1L, List.of("#nature"), List.of("Nature"), 10, 100, LocalDateTime.now())
		);
		Page<PostDto.PostDtoResponse> page = new PageImpl<>(postList, pageable, postList.size());

		when(postService.findAllPostsByCategoryAndRGB(anyString(), any(int[].class), any(Pageable.class)))
			.thenReturn(page);

		PostPublicController controller = new PostPublicController(postService, imageRedisService);

		// Act
		ResponseEntity<Page<PostDto.PostDtoResponse>> response = controller.searchByCategoryOrRgbPost(pageable, condition);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
	}



	// API returns 200 OK with an empty list when no posts match the category and RGB color
	@Test
	public void test_no_matching_posts() throws JsonProcessingException {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
		CategoryAndColorSearchCondition condition = new CategoryAndColorSearchCondition();
		condition.setCategory("NonExistentCategory");
		condition.setRgbColor(new int[]{0, 0, 0});

		Page<PostDto.PostDtoResponse> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

		when(postService.findAllPostsByCategoryAndRGB(anyString(), any(int[].class), any(Pageable.class)))
			.thenReturn(emptyPage);

		PostPublicController controller = new PostPublicController(postService, imageRedisService);

		// Act
		ResponseEntity<Page<PostDto.PostDtoResponse>> response = controller.searchByCategoryOrRgbPost(pageable, condition);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
	}


	// Retrieves post details successfully when a valid post ID is provided
	@Test
	public void test_get_post_by_id_success() throws JsonProcessingException {
		// Arrange
		Long validPostId = 1L;
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		PostDto.PostDetailDtoResponse expectedResponse = new PostDto.PostDetailDtoResponse(
			"nickname", validPostId, 1L, "postContent", List.of("hashtag1"), List.of("category1"), 10, true, 100, LocalDateTime.now(), LocalDateTime.now()
		);

		when(postService.getPostById(validPostId)).thenReturn(expectedResponse);

		PostPublicController controller = new PostPublicController(postService, imageRedisService);

		// Act
		ResponseEntity<PostDto.PostDetailDtoResponse> responseEntity = controller.getPostById(validPostId, request, response);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(expectedResponse, responseEntity.getBody());
	}
	// Returns like count for a valid post ID
	@Test
	public void test_like_count_valid_post_id() {
		PostPublicController controller = new PostPublicController(postService, imageRedisService);
		PostDto.PostIdRequest request = new PostDto.PostIdRequest(1L);

		when(postService.likeCount(1L)).thenReturn(10);

		ResponseEntity<Integer> response = controller.likeCount(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(10, response.getBody());
	}

	// Returns posts for a valid category ID
	@Test
	public void test_returns_posts_for_valid_category_id() {
		// Arrange
		Long categoryId = 1L;
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
		List<PostDto.PostDtoResponse> postList = List.of(
			new PostDto.PostDtoResponse("user1", 1L, 1L, List.of("#tag1"), List.of("category1"), 10, 100, LocalDateTime.now())
		);
		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(postList, pageable, postList.size());

		when(postService.findAllPostsByCategory(categoryId, pageable)).thenReturn(expectedPage);

		PostPublicController controller = new PostPublicController(postService, imageRedisService);

		// Act
		ResponseEntity<Page<PostDto.PostDtoResponse>> response = controller.getPostsByCategory(categoryId, pageable);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPage, response.getBody());
	}
	// Should return a list of all categories when the endpoint is called
	@Test
	public void test_get_category_all_returns_list_of_categories() {
		// Arrange
		PostPublicController controller = new PostPublicController(postService, imageRedisService);
		List<CategoryEntity> mockCategories = Arrays.asList(
			new CategoryEntity("T-Shirts"),
			new CategoryEntity("Pants")
		);
		when(postService.getAllCategory()).thenReturn(mockCategories);

		// Act
		ResponseEntity<List<CategoryEntity>> response = controller.getCategoryAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		assertEquals("T-Shirts", response.getBody().get(0).getCategoryContent());
		assertEquals("Pants", response.getBody().get(1).getCategoryContent());
	}


	// Returns a list of popular colors with status 200
	@Test
	public void test_returns_list_of_popular_colors_with_status_200() {
		PostPublicController postPublicController = new PostPublicController(postService, imageRedisService);

		List<ColorDto.ColorPopularResponse> mockResponse = List.of(
			new ColorDto.ColorPopularResponse("Red", 255, 0, 0),
			new ColorDto.ColorPopularResponse("Green", 0, 255, 0)
		);

		when(imageRedisService.getPopularColorList()).thenReturn(mockResponse);

		ResponseEntity<List<ColorDto.ColorPopularResponse>> response = postPublicController.getPopularColor();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockResponse, response.getBody());
	}


}