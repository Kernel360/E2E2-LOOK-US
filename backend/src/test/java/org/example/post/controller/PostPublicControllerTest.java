package org.example.post.controller;

import org.example.post.domain.dto.PostDto;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.service.PostService;
import org.example.user.controller.member.UserApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class}) // Spring Context 로드, Mockito를 사용해서 Mock 객체 주입
@WebMvcTest(UserApiController.class) // Spring MVC테스트를 위한 설정
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
	@DisplayName("모든 게시글 조회 findAllPosts 정상 리턴 확인 테스트")
	void searchPost_shouldReturnPostPage() {
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
	@DisplayName("Post Id 기반 게시글 조회 getPostById 정상 리턴 확인 테스트")
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
