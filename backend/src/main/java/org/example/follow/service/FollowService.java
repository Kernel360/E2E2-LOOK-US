package org.example.follow.service;

import java.util.ArrayList;
import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
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
		UserEntity fromUser = this.getUserByEmail(email);
		UserEntity toUser = this.getUserByNickname(nickname);

		if (fromUser.equals(toUser)) {
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
				.build();
		}

		followRepository
			.findByFromUserAndToUser(fromUser, toUser)
			.ifPresent((follow) -> {
				throw ApiUserException.builder()
					.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
					.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
					.setErrorData(() -> String.format("%s --> %s", follow.getFromUser(), follow.getToUser()))
					.build();
			});

		Follow follow = Follow.builder()
			.fromUser(fromUser)
			.toUser(toUser)
			.build();

		followRepository.save(follow);
		return "Success";
	}

	public String unFollow(String email, String nickname) {
		UserEntity fromUser = this.getUserByEmail(email);
		UserEntity toUser = this.getUserByNickname(nickname);

		Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
										.orElseThrow(
											() -> ApiUserException.builder()
												.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
												.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
												.build()
										);
		followRepository.delete(follow);

		return "Success";
	}

	public List<FollowResponseDto> followingList(String targetUserName) {
		UserEntity targetUser = this.getUserByNickname(targetUserName);
		List<Follow> list = followRepository.findByFromUser(targetUser);
		List<FollowResponseDto> followList = new ArrayList<>();

		for (Follow follow : list) {
			UserEntity toUser = follow.getToUser();

			followList.add(new FollowResponseDto(
				toUser.getNickname(),
				followRepository.findByToUser(toUser).size(),
				toUser.getProfileImageId()
			));
		}

		return followList;
	}

	public List<FollowResponseDto> followerList(String targetUserName) {
		UserEntity targetUser = this.getUserByNickname(targetUserName);
		List<Follow> list = followRepository.findByToUser(targetUser);
		List<FollowResponseDto> followList = new ArrayList<>();

		for (Follow follow : list) {
			UserEntity fromUser = follow.getFromUser();

			followList.add(new FollowResponseDto(
				fromUser.getNickname(),
				followRepository.findByToUser(fromUser).size(),
				fromUser.getProfileImageId()
			));
		}

		return followList;
	}

	private UserEntity getUserByEmail(String email) throws ApiUserException {
		return userRepository.findByEmail(email)
							 .orElseThrow(
								 () -> ApiUserException.builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
									 .build()
							 );
	}

	private UserEntity getUserByNickname(String nickname) throws ApiUserException {
		return userRepository.findByNickname(nickname)
							 .orElseThrow(
								 () -> ApiUserException.builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
									 .build()
							 );
	}
}
