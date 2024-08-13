package org.example.post.controller;

import org.example.post.domain.dto.PostDto;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PostPublicControllerTest {

	@Mock
	private PostService postService;

	@InjectMocks
	private PostPublicController postPublicController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void searchPost_shouldReturnPosts() {
		// Arrange
		PostSearchCondition postSearchCondition = new PostSearchCondition();
		Pageable pageable = PageRequest.of(0, 10);
		Page<PostDto.PostDtoResponse> expectedPage = new PageImpl<>(Collections.emptyList());

		when(postService.findAllPosts(postSearchCondition, pageable)).thenReturn(expectedPage);

		// Act
		ResponseEntity<Page<PostDto.PostDtoResponse>> responseEntity = postPublicController.searchPost(postSearchCondition, pageable);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(expectedPage, responseEntity.getBody());
	}

	@Test
	void getPostById_shouldReturnPost() {
		// Arrange
		Long postId = 1L;
		PostDto.PostDetailDtoResponse expectedResponse = new PostDto.PostDetailDtoResponse("nickname", postId, 1L, "content", Collections.emptyList(), 0, null, null);

		when(postService.getPostById(postId)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<PostDto.PostDetailDtoResponse> responseEntity = postPublicController.getPostById(postId);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(expectedResponse, responseEntity.getBody());
	}
}
