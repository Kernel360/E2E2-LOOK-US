package org.example.follow.controller;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.follow.service.FollowService;
import org.example.config.log.LogExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/a1/me/follow")
public class FollowPublicController {

	private final FollowService followService;

	/**
	 * 특정 사용자의 팔로우/팔로워 리스트 조회
	 * GET
	 */
	@GetMapping("/relation")
	@LogExecution
	public ResponseEntity<?> getFollowList(
		@RequestParam("type") String followType,
		@RequestParam("nickname") String targetUserNickname
	) {
		if (followType.equals("followers")) { //targetUser를 팔로우하는 사람들 목록 조회 (팔로워 조회)
			return ResponseEntity.ok().body(followService.followerList(targetUserNickname));
		}

		if (followType.equals("followings")) { //targetUser가 팔로우 하고있는 사람들 목록 조회 (팔로잉 조회)
			return ResponseEntity.ok().body(followService.followingList(targetUserNickname));
		}

		throw ApiUserException.builder()
			.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
			.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
			.setErrorData(() -> String.format("follow type = %s", followType))
			.build();
	}}
