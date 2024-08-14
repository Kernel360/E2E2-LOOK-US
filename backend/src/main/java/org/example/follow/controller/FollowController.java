package org.example.follow.controller;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.follow.domain.dto.FollowRequestDto;
import org.example.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me/follow")
public class FollowController {

	private final FollowService followService;

	/**
	 * 팔로우, 언팔로우
	 */
	@PutMapping("")
	public ResponseEntity<?> handleFollow(
		@RequestBody FollowRequestDto requestDto
	) {
		String fromUser = SecurityContextHolder.getContext().getAuthentication().getName();
		String toUser = requestDto.getNickname();

		switch (requestDto.getFollowStatus()) {
			case FOLLOW -> followService.follow(fromUser, toUser);
			case UNFOLLOW -> followService.unFollow(fromUser, toUser);
			default ->
				throw ApiUserException
					.builder()
					.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
					.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
					.setErrorData(() -> String.format("follow status = %s", requestDto.getFollowStatus()))
					.build();
		}

		return ResponseEntity.ok().build();
	}
}
