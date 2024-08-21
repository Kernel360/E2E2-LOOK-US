package org.example.follow.service;

import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.follow.domain.dto.FollowDto;
import org.example.follow.domain.entity.Follow;
import org.example.follow.domain.enums.FollowStatus;
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

	// follow 거는 경우
	public void follow(String fromUser_Email, String toUser_Nickname) {
		UserEntity fromUser = this.getUserByEmail(fromUser_Email);
		UserEntity toUser = this.getUserByNickname(toUser_Nickname);

		if (fromUser.equals(toUser)) {
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
				.build();
		}

		followRepository.findByFromUserAndToUser(fromUser, toUser)
						.ifPresent((follow) -> {
							throw ApiUserException.builder()
								.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
								.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
								.setErrorData(() -> String.format("%s --> %s", follow.getFromUser(), follow.getToUser()))
								.build();
						});

		followRepository.save(
			Follow.builder().fromUser(fromUser).toUser(toUser).build()
		);
	}

	public void unFollow(String fromUser_Email, String toUser_Nickname) {
		UserEntity fromUser = this.getUserByEmail(fromUser_Email);
		UserEntity toUser = this.getUserByNickname(toUser_Nickname);

		if (fromUser.equals(toUser)) {
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiUserErrorSubCategory.USER_FOLLOW_INVALID_REQUEST)
				.build();
		}

		followRepository.deleteByFromUserAndToUser(fromUser, toUser);
	}

	public FollowDto.FollowListResponse followingList(String targetUserName) {
		UserEntity targetUser = this.getUserByNickname(targetUserName);
		List<FollowDto.FollowUser> followings = followRepository.findByFromUser(targetUser)
			.stream()
			.map(Follow::getToUser) // 팔로잉하는 사용자 목록을 가져옴
			.map(following -> new FollowDto.FollowUser(
				following.getNickname(),
				this.getFollowerCountOfUser(following),
				following.getProfileImageId()
			)).toList();

		return new FollowDto.FollowListResponse(followings.size(), followings);
	}



	public FollowDto.FollowListResponse followerList(String targetUserName) {
		UserEntity targetUser = this.getUserByNickname(targetUserName);
		List<FollowDto.FollowUser> followers = followRepository.findByToUser(targetUser)
			.stream()
			.map(Follow::getFromUser) // 팔로워 목록을 가져옴
			.map(follower -> new FollowDto.FollowUser(
				follower.getNickname(),
				this.getFollowerCountOfUser(follower),
				follower.getProfileImageId()
			)).toList();

		return new FollowDto.FollowListResponse(followers.size(), followers);
	}

	// 두 사용자의 팔로우 상태를 반환하는 메서드
	public FollowStatus getFollowStatus(String fromUser_Email, String toUser_Nickname) {
		UserEntity fromUser = this.getUserByEmail(fromUser_Email);
		UserEntity toUser = this.getUserByNickname(toUser_Nickname);

		return followRepository.findByFromUserAndToUser(fromUser, toUser)
			.map(follow -> FollowStatus.FOLLOW)  // 팔로우 관계가 존재하면 FOLLOW
			.orElse(FollowStatus.UNFOLLOW);      // 존재하지 않으면 UNFOLLOW
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

	private int getFollowerCountOfUser(UserEntity user) {
		return this.followRepository.findByToUser(user).size();
	}
}
