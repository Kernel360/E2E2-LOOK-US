package org.example.user.service.token;

import java.time.Duration;

import org.example.config.jwt.TokenProvider;
import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.service.member.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	public String createNewAccessToken(String refreshToken) {
		// 토큰 유효성 검사에 실패하면 예외 발생
		if (!tokenProvider.validToken(refreshToken)) {
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_UNAUTHORIZED)
				.subCategory(ApiUserErrorSubCategory.USER_INVALID_REFRESH_TOKEN)
				.build();
		}

		Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
		UserEntity user = userService.getUserById(userId);

		return tokenProvider.generateToken(user, Duration.ofHours(2));
	}
}
