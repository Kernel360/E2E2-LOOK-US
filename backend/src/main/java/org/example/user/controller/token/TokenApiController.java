package org.example.user.controller.token;

import org.example.user.domain.dto.request.token.CreateAccessTokenRequest;
import org.example.user.domain.dto.response.token.CreateAccessTokenResponse;
import org.example.user.service.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
	private final TokenService tokenService;

	@PostMapping("/api/token")
	public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
		@RequestBody CreateAccessTokenRequest request) {
		String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new CreateAccessTokenResponse(newAccessToken));
	}
}
