package org.example.user.controller.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.example.user.domain.dto.UserDto;
import org.example.user.service.member.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(UserViewController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class UserViewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private WebApplicationContext webApplicationContext;


	protected MediaType contentType =
		new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			StandardCharsets.UTF_8);

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}


	@DisplayName("메인 화면 Controller Test")
	@Test
	void testHello() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"));
	}

	@DisplayName("OAuthLogin 화면 Controller Test")
	@Test
	void testLogin() throws Exception {
		mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("oauthLogin"));
	}

	@DisplayName("회원가입 화면 Controller Test")
	@Test
	void testSignup() throws Exception {
		mockMvc.perform(get("/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"));
	}

	@DisplayName("일반 폼 회원가입 성공 Controller Test")
	@Test
	void signup_User_Success() throws Exception {
		// given
		UserDto.UserCreateRequest userCreateRequest = new UserDto.UserCreateRequest("test@gmail.com", "password");

		// when
		mockMvc.perform(post("/user")
				.contentType(contentType)
				.content("{\"email\":\"test@gmail.com\", \"password\":\"password\"}"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));

		// then
		Mockito.verify(userService, Mockito.times(1)).saveUser(userCreateRequest);
	}

	@DisplayName("일반 폼 로그아웃 성공 Controller Test")
	@Test
	void logout_User_Success() throws Exception {
		mockMvc.perform(get("/logout"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}

	@DisplayName("회원 로그인 성공 후 보여지는 페이지 Controller Test")
	@Test
	void getArticles_User_Success() throws Exception {
		mockMvc.perform(get("/articles"))
			.andExpect(status().isOk())
			.andExpect(view().name("articleList"));
	}








}
