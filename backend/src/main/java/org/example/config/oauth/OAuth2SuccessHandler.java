package org.example.config.oauth;

import java.io.IOException;
import java.time.Duration;

import org.example.config.jwt.TokenProvider;
import org.example.log.LogExecution;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.entity.token.RefreshToken;
import org.example.user.repository.token.RefreshTokenRepository;
import org.example.user.service.member.UserService;
import org.example.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
	public static final String ACCESS_TOKEN_COOKIE_NAME = "token";

	public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
	public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

	public static final String OAUTH2_SUCCESS_REDIRECT_PATH = "http://localhost:3000";

	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
	private final UserService userService;

	@Override
	@LogExecution
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		UserEntity user = userService.getUserByEmail((String)oAuth2User.getAttributes().get("email"));

		String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
		saveRefreshToken(user.getUserId(), refreshToken);
		addRefreshTokenToCookie(request, response, refreshToken);

		String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
		addAccessTokenToCookie(request, response, accessToken);

		clearAuthenticationAttributes(request, response);

		getRedirectStrategy().sendRedirect(request, response, OAUTH2_SUCCESS_REDIRECT_PATH);
	}

	@LogExecution
	private void saveRefreshToken(Long userId, String newRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
			.map(entity -> entity.update(newRefreshToken))
			.orElse(new RefreshToken(userId, newRefreshToken));

		refreshTokenRepository.save(refreshToken);
	}

	@LogExecution
	private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
		String refreshToken) {
		int cookieMaxAge = (int)REFRESH_TOKEN_DURATION.toSeconds();

		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
		CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
	}

	@LogExecution
	private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response,
		String accessToken) {
		int cookieMaxAge = (int)ACCESS_TOKEN_DURATION.toSeconds();

		CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
		CookieUtil.addCookie(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, cookieMaxAge);
	}

	@LogExecution
	private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}
}
