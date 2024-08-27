package org.example.follow.domain.dto;

import java.util.List;

import org.example.follow.domain.enums.FollowStatus;

public class FollowDto {

	public record FollowRequest(
		String nickname,
		FollowStatus followStatus
	) {}

	public record FollowListResponse(
		List<FollowUser> followers
	) {}

	public record FollowUser(
		String nickname,
		int followersCount,
		Long profileImageLocationId
	) {}
}
