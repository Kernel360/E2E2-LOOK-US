package org.example.follow.service;

import java.util.ArrayList;
import java.util.List;

import org.example.follow.domain.dto.FollowResponseDto;
import org.example.follow.domain.entity.Follow;
import org.example.follow.repository.FollowRepository;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class FollowService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	//follow 거는 경우
	public String follow(String email, String nickname) {
		UserEntity fromUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		UserEntity toUser = userRepository.findByNickname(nickname)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		if (fromUser.equals(toUser)) {
			throw new IllegalArgumentException("You cannot follow yourself");
		}

		followRepository.findByFromUserAndToUser(fromUser, toUser)
			.ifPresent(f -> { throw new IllegalArgumentException("Already following this user"); });

		Follow follow = Follow.builder()
			.fromUser(fromUser)
			.toUser(toUser)
			.build();

		followRepository.save(follow);
		return "Success";
	}

	public String unFollow(String email, String nickname) {
		UserEntity fromUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found")); //팔로우하려는 사용자의 정보가 없을 경우 예외처리

		UserEntity toUser = userRepository.findByNickname(nickname)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		//fromUser, toUser간 팔로우 관계 삭제하기
		Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
			.orElseThrow(() -> new IllegalArgumentException("Follow relation not found"));

		followRepository.delete(follow);
		return "Success";
	}

	public List<FollowResponseDto> followingList(String targetUserName, UserEntity requestUser) {
		UserEntity targetUser = userRepository.findByNickname(targetUserName)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		List<Follow> list = followRepository.findByFromUser(targetUser);
		List<FollowResponseDto> followList = new ArrayList<>();

		for (Follow follow : list) {
			UserEntity toUser = follow.getToUser();
			int followersCount = followRepository.findByToUser(toUser).size();
			String status = findStatus(toUser, requestUser);
			followList.add(new FollowResponseDto(
				toUser.getNickname(),
				followersCount,
				toUser.getProfileImageId()
			));
		}

		return followList;
	}

	public List<FollowResponseDto> followerList(String targetUserName, UserEntity requestUser) {
		UserEntity targetUser = userRepository.findByNickname(targetUserName)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		List<Follow> list = followRepository.findByToUser(targetUser);
		List<FollowResponseDto> followList = new ArrayList<>();

		for (Follow follow : list) {
			UserEntity fromUser = follow.getFromUser();
			int followersCount = followRepository.findByToUser(fromUser).size();
			String status = findStatus(fromUser, requestUser);
			followList.add(new FollowResponseDto(
				fromUser.getNickname(),
				followersCount,
				fromUser.getProfileImageId()
			));
		}

		return followList;
	}

	//A와 B의 follow관계 찾기
	private String findStatus(UserEntity selectedUser, UserEntity requestUser) {
		if (selectedUser.equals(requestUser)) {
			return "self";
		}
		return followRepository.findByFromUserAndToUser(requestUser, selectedUser).isPresent() ? "following" : "none";
	}
}
