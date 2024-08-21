package org.example.follow.domain.dto;

import java.util.List;

import org.example.follow.domain.enums.FollowStatus;

public class FollowDto {

	public record FollowRequest(
		String nickname,
		FollowStatus followStatus
	) {}

	public record FollowListResponse(
		int totalCount,  // 총 팔로워/팔로잉 수
		List<FollowUser> followers
	) {}

	public record FollowUser(
		String nickname,
		int followersCount,
		Long profileImageId
	) {}

	public record FollowStatusRequest(
		String nickname
	) {}

	public record FollowStatusResponse(
		String followStatus
	) {}
}
