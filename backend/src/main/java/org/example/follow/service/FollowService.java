package org.example.follow.service;

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
	public String follow(String email, Long userId) {
		UserEntity fromUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found")); //팔로우하려는 사용자의 정보가 없을 경우 예외처리

		UserEntity toUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

		if (fromUser == toUser) {
			throw new IllegalArgumentException("자기 자신을 follow할 수 없습니다.");
		}
		//중복 follow 방지
		if (followRepository.findFollow(fromUser, toUser).isPresent())
			throw new IllegalArgumentException("이미 follow 했습니다.");

		Follow follow = Follow.builder()
			.fromUser(fromUser)
			.toUser(toUser)
			.build();

		followRepository.save(follow);


		return "Success";
	}

	public String unFollow(String email, Long userId) {
		UserEntity fromUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found")); //팔로우하려는 사용자의 정보가 없을 경우 예외처리

		UserEntity toUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

		//fromUser, toUser간 팔로우 관계 삭제하기
		Follow follow = followRepository.findFollow(fromUser, toUser).orElseThrow(() -> new IllegalArgumentException("Follow relation not found"));

		followRepository.delete(follow);
		return "Success";
	}
}
