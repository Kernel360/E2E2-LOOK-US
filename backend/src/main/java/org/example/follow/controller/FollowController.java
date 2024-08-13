package org.example.follow.controller;

import org.example.follow.domain.dto.FollowRequestDto;
import org.example.follow.domain.enums.FollowStatus;
import org.example.follow.service.FollowService;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.service.member.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
public class FollowController {
	private final UserService userService;
	private final FollowService followService;

	/**
	 * 팔로우, 언팔로우
	 */
	@PutMapping("/follow")
	public ResponseEntity handleFollow(
		@RequestBody FollowRequestDto requestDto
	) {
		String fromUser = SecurityContextHolder.getContext().getAuthentication().getName();
		String toUser = requestDto.getNickname();
		switch (requestDto.getFollowStatus()) {
			case FOLLOW:
				followService.follow(fromUser, toUser);
				break;
			case UNFOLLOW:
				followService.unFollow(fromUser, toUser);
				break;
			default:
				return ResponseEntity.badRequest().body("Invalid status value");
		}

		return ResponseEntity.ok().build();
	}


	/**
	 * 특정 사용자의 팔로우/팔로워 리스트 조회
	 * GET
	 */
	@GetMapping("/follow/relation")
	public ResponseEntity getFollowList(
		@RequestParam("type") String followType,
		@RequestParam("nickname") String targetUserNickname
	) {
		String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity loggedInUser = userService.findByEmail(loggedInUserEmail);
		if (followType.equals("followers")) { //targetUser를 팔로우하는 사람들 목록 조회 (팔로워 조회)
			return ResponseEntity.ok().body(followService.followerList(targetUserNickname, loggedInUser));
		} else if (followType.equals("followings")) { //targetUser가 팔로우 하고있는 사람들 목록 조회 (팔로잉 조회)
			return ResponseEntity.ok().body(followService.followingList(targetUserNickname, loggedInUser));
		}

		return ResponseEntity.badRequest().body("Invalid follow type");
	}


}
