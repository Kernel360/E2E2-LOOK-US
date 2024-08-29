package org.example.user.service.token;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.config.log.LogExecution;
import org.example.user.domain.entity.token.RefreshToken;
import org.example.user.repository.token.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@LogExecution
	public RefreshToken findByRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken)
									 .orElseThrow(() -> ApiUserException.builder()
										 .category(ApiErrorCategory.RESOURCE_UNAUTHORIZED)
										 .subCategory(ApiUserErrorSubCategory.USER_REFRESH_TOKEN_NOT_FOUND)
										 .build());
	}

}
