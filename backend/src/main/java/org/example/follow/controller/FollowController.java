package org.example.follow.controller;

import org.example.follow.domain.enums.FollowStatus;
import org.example.follow.service.FollowService;
import org.example.user.service.member.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@PutMapping("/follows/{user_id}")
	public ResponseEntity handleFollow(
		@PathVariable("user_id") Long user_id,
		@RequestBody FollowRequest followRequest
	) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		FollowStatus status = FollowStatus.fromStatus(followRequest.getStatus());

		switch (status) {
			case FOLLOW:
				followService.follow(email, user_id);
				break;
			case UNFOLLOW:
				followService.unFollow(email, user_id);
				break;
			default:
				return ResponseEntity.badRequest().body("Invalid status value");
		}

		return ResponseEntity.ok().build();
	}


	// Request Body로 받을 클래스 정의
	public static class FollowRequest {
		private int status;

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}

	/**
	 * 나의 전체 팔로우/팔로워 리스트 조회
	 * GET
	 */
/*	@GetMapping("/relation")
	public ResponseEntity getFollowList(
		@RequestParam("type") String followType
	) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if (followType.equals("followers")) {
			followService.followerList()
		} else if (followType.equals("followings")) {


		}

	}*/


}
