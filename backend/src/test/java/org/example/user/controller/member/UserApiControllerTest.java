package org.example.user.controller.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.repository.member.UserRepository;
import org.example.user.service.member.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith({SpringExtension.class, MockitoExtension.class}) // Spring Context 로드, Mockito를 사용해서 Mock 객체 주입
@WebMvcTest(UserApiController.class) // Spring MVC테스트를 위한 설정
class UserApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper; // JSON 데이터를 객체로 변환하거나 객체를 JSON으로 변환

	@MockBean // 실제 서비스나 레포지토리 사용하지 않고 모킹된 동작함
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	private UserEntity user;
	private UserDto.UserUpdateResponse updateResponse;

	@BeforeEach
	void setUp() {
		user = UserEntity.builder()
			.username("username")
			.password("password")
			.email("test@gmail.com")
			.build();

		updateResponse = new UserDto.UserUpdateResponse(1L);

		// 'SecurityContextHolder' 를 사용해서 인증된 사용자 정보 설정 (실제로 인증된 사용자 처럼 행동)
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
		);
	}

	@DisplayName("소셜로그인_성공_후_회원정보_업데이트")
	@Test
	void userUpdate_Success() throws Exception {
		final String url = "/api/v1/user/update";
		final String birth = "1997-11-20";
		final String instaId = "limbaba";
		final String nickName = "limba";
		final Gender gender = Gender.GENDER_MAN;

		final UserDto.UserUpdateRequest userUpdateRequest = new UserDto.UserUpdateRequest(birth, instaId, nickName,
			gender);

		// 프로필 이미지와 업데이트 요청을 나타내는 MockMultipartFile 객체를 생성함
		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "image.jpg", MediaType.IMAGE_JPEG_VALUE,
			"image content".getBytes());
		MockMultipartFile updateRequestPart = new MockMultipartFile("updateRequest", null, "application/json",
			objectMapper.writeValueAsString(userUpdateRequest).getBytes());

		// userService의 updateUser 메서드가 호출될 때 updateResponse 를 반환
		given(userService.updateUser(any(UserDto.UserUpdateRequest.class), anyString(), any(MockMultipartFile.class)))
			.willReturn(updateResponse);

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

		Principal principal = Mockito.mock(Principal.class); // 인증된 사용자 설정

		// multipart 요청의 메서드를 PATCH 로 설정
		ResultActions result = mockMvc.perform(multipart(url)
			.file(profileImage)
			.file(updateRequestPart)
			.with(request -> {
				request.setMethod("PATCH");
				return request;
			})
			.principal(principal)
			.header(HttpHeaders.AUTHORIZATION, "Bearer token")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.with(csrf())); // csrf 토큰을 추가해야한다. 테스트 환경에서 CSRF 보호를 비활성화 하지 않을 경우 에러가남

		// 응답 코드 확인 후 응답 본문에 있는 userId 값 확인
		result.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId", Matchers.is(updateResponse.userId().intValue())));
	}

	@DisplayName("회원 정보 업데이트 예외 테스트")
	@Test
	void userUpdate_Fail() throws Exception {

		// userService의 updateUser 메서드가 호출될 때 updateResponse 를 반환
		given(userService.updateUser(isNull(), anyString(), isNull()))
			.willThrow(
				ApiUserException.builder()
					.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
					.subCategory(ApiUserErrorSubCategory.USER_UPDATE_IMPOSSIBLE)
					.build()
			);
	}

	@DisplayName("내 게시글 조회 테스트")
	@Test
	void testGetMyPost() throws Exception {
		UserDto.UserGetPostsResponse postResponse = new UserDto.UserGetPostsResponse(1L, "Post Content", List.of("newtest", "test"), 0);
		List<UserDto.UserGetPostsResponse> responseList = Collections.singletonList(postResponse);
		Principal principal = Mockito.mock(Principal.class);

		when(userService.getMyPosts(eq("test@gmail.com"))).thenReturn(responseList);

		// MockMvc를 사용한 GET 요청 및 응답 검증
		mockMvc.perform(get("/api/v1/user/me/posts")
				.header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.principal(principal))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].imageId").value(1))
			.andExpect(jsonPath("$[0].postContent").value("Post Content"))
			.andExpect(jsonPath("$[0].hashtags[0]").value("newtest"))
			.andExpect(jsonPath("$[0].hashtags[1]").value("test"))
			.andExpect(jsonPath("$[0].likeCount").value(0));
	}

	@DisplayName("내 정보 조회 테스트")
	@Test
	void testGetMyPage() throws Exception{
		UserDto.UserGetInfoResponse userGetInfoResponse = new UserDto.UserGetInfoResponse(
			"username",
			"test@gmail.com",
			Gender.GENDER_MAN,
			"1999-9-9",
			"nickname",
			"instaidid",
			1
		);
		Principal principal = Mockito.mock(Principal.class);

		when(userService.getMyInfo(eq("test@gmail.com")))
			.thenReturn(userGetInfoResponse);

		// MockMvc를 사용한 GET 요청 및 응답 검증
		mockMvc.perform(get("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.principal(principal))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("username"))
			.andExpect(jsonPath("$.email").value("test@gmail.com"))
			.andExpect(jsonPath("$.nickname").value("nickname"))
			.andExpect(jsonPath("$.postNum").value(1));
	}

	@DisplayName("회원 탈퇴 테스트")
	@Test
	void testResignUser() throws Exception {

		Principal principal = Mockito.mock(Principal.class);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

		doNothing().when(userService).resignUser(anyString(), eq(request), eq(response));


		mockMvc.perform(post("/api/v1/user/resign")
				.header(HttpHeaders.AUTHORIZATION, "Bearer token")
				.principal(principal)
				.with(csrf()))
			.andExpect(status().isAccepted());

	}
}