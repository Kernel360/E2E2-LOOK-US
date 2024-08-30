package org.example.post.controller;

import static org.hamcrest.core.StringContains.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.post.domain.dto.PostDto;
import org.example.post.service.PostService;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(PostApiController.class)
public class PostApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;  // 서비스 계층을 Mocking합니다.

	@Autowired
	private ObjectMapper objectMapper;

	private UserEntity user;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
		user = UserEntity.builder()
			.username("username")
			.password("password")
			.email("test@gmail.com")
			.role(Role.ROLE_USER)
			.build();
		// Security Context 설정
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
			user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	@WithMockUser(username = "username", roles = {"USER"}) // Mock 유저 설정 추가
	public void testCreatePost() throws Exception {
		final String url = "/api/v1/posts";
		// Given
		PostDto.CreatePostDtoRequest requestDto = new PostDto.CreatePostDtoRequest(
			"cont",  // 게시글 내용
			"hash",  // 해시태그
			"cate"   // 카테고리
		);
		PostDto.CreatePostDtoResponse responseDto = new PostDto.CreatePostDtoResponse(
			1L  // 생성된 게시글의 ID
		);
		String requestDtoJson = "{\"content\": \"cont\", \"hashtag\": \"hash\", \"category\": \"cate\"}";

		MockMultipartFile userRequestPart = new MockMultipartFile(
			"userRequest",
			"userRequest",
			"application/json",
			requestDtoJson.getBytes()
		);
		MockMultipartFile image = new MockMultipartFile(
			"image", // 필드명
			"image.jpg", // 파일명
			"image/jpeg", // MIME 타입
			"test image content".getBytes() // 이미지 내용 (바이트 배열)
		);
		when(postService.createPost(any(PostDto.CreatePostDtoRequest.class), anyString(), any(MultipartFile.class)))
			.thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(multipart("/api/v1/posts")
				.file(image)
				.file(userRequestPart) // Sending the JSON request object as part
				.with(csrf()) // Adding CSRF token
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.postId").value(1L));

		// Verify that the service method was called
		verify(postService, times(1)).createPost(any(PostDto.CreatePostDtoRequest.class), anyString(),
			any(MultipartFile.class));
	}


	@Test
	@WithMockUser(username = "username", roles = {"USER"})
	public void testUpdatePost() throws Exception {
		final String url = "/api/v1/posts/1"; // Assuming post ID is 1

		// Create request DTO and response DTO
		PostDto.CreatePostDtoRequest updateRequestDto = new PostDto.CreatePostDtoRequest(
			"updated content",
			"updated hashtag",
			"updated category"
		);

		PostDto.CreatePostDtoResponse responseDto = new PostDto.CreatePostDtoResponse(1L);

		// Convert DTO to JSON
		String updateRequestJson = objectMapper.writeValueAsString(updateRequestDto);

		// Create MockMultipartFile instances
		MockMultipartFile updateRequestPart = new MockMultipartFile(
			"updateRequest",  // 이름을 "updateRequest"로 수정
			"updateRequest",
			"application/json",
			updateRequestJson.getBytes()
		);

		MockMultipartFile image = new MockMultipartFile(
			"image",
			"image.jpg",
			"image/jpeg",
			"updated image content".getBytes()
		);

		// Define behavior for postService
		when(postService.updatePost(any(PostDto.CreatePostDtoRequest.class), any(MultipartFile.class), anyString(),
			anyLong()))
			.thenReturn(responseDto);

		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.multipart(url)
				.file(updateRequestPart)  // 수정된 이름 사용
				.file(image)
				.with(csrf())
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
				.with(request -> {
					request.setMethod(HttpMethod.PATCH.name());
					return request;
				}))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.postId").value(1L));

		// Verify that the service method was called
		verify(postService, times(1)).updatePost(any(PostDto.CreatePostDtoRequest.class), any(MultipartFile.class),
			anyString(), anyLong());
	}

	@Test
	@WithMockUser(username = "username", roles = {"USER"})
	public void testLikePost() throws Exception {
		final String url = "/api/v1/posts/likes"; // 좋아요 API의 URL

		// Create request DTO and response
		PostDto.PostIdRequest likeRequestDto = new PostDto.PostIdRequest(1L); // 게시글 ID를 1로 설정
		Boolean expectedResponse = true; // 테스트에서 예상되는 응답 (예를 들어, 좋아요가 성공적으로 수행됨)

		// Convert DTO to JSON
		String likeRequestJson = objectMapper.writeValueAsString(likeRequestDto);

		// Define behavior for postService
		when(postService.like(anyLong(), anyString()))
			.thenReturn(expectedResponse);

		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.patch(url)
				.contentType(MediaType.APPLICATION_JSON) // 요청 본문이 JSON 타입임을 명시
				.content(likeRequestJson) // 요청 본문으로 JSON 데이터 포함
				.with(csrf()) // CSRF 토큰 추가
				.accept(MediaType.APPLICATION_JSON)) // 응답으로 JSON을 받겠다고 명시
			.andDo(print())
			.andExpect(status().isOk()) // HTTP 상태 코드가 200 OK인지 확인
			.andExpect(content().string(expectedResponse.toString())); // 응답 본문이 예상한 값과 일치하는지 확인

		// Verify that the service method was called
		verify(postService, times(1)).like(anyLong(), anyString());
	}




	@Test
	@WithMockUser(username = "username", roles = {"USER"})
	public void testDeletePost() throws Exception {
		final String url = "/api/v1/posts";

		PostDto.PostIdRequest deleteRequestDto = new PostDto.PostIdRequest(1L); // Assuming post ID is 1

		doNothing().when(postService).delete(anyLong(), anyString());

		mockMvc.perform(delete("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postId\": 1}")
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(postService, times(1)).delete(anyLong(), anyString());
	}

}
