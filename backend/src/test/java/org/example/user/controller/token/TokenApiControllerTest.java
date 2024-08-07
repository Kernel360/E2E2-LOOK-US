package org.example.user.controller.token;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.example.config.jwt.JwtFactory;
import org.example.config.jwt.JwtProperties;
import org.example.user.domain.dto.request.token.CreateAccessTokenRequest;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.entity.token.RefreshToken;
import org.example.user.repository.member.UserRepository;
import org.example.user.repository.token.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	JwtProperties jwtProperties;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.build();
		userRepository.deleteAll();
	}

	@DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
	@Test
	public void createNewAccessToken() throws Exception {
		// given
		final String url = "/api/token";

		UserEntity testUser = userRepository.save(UserEntity.builder()
			.email("user@gmail.com")
			.password("test")
			.build());

		String refreshToken = JwtFactory.builder()
			.claims(Map.of("id", testUser.getUserId()))
			.build()
			.createToken(jwtProperties);

		refreshTokenRepository.save(new RefreshToken(testUser.getUserId(), refreshToken));

		CreateAccessTokenRequest request = new CreateAccessTokenRequest();
		request.setRefreshToken(refreshToken);
		final String requestBody = objectMapper.writeValueAsString(request);

		// when
		ResultActions resultActions = mockMvc.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(requestBody));

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.accessToken").isNotEmpty());
	}

}