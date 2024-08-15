package org.example.follow.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.example.follow.domain.enums.FollowStatus;

public class FollowDto {

	public record FollowRequestDto(
		String nickname,
		FollowStatus followStatus
	) {}

	public record GetFollowListResponseDto(
		List<FollowUserDto> followers
	) {}

	public record FollowUserDto(
		String nickname,
		int followersCount,
		Long profileImageId
	) {}
}
