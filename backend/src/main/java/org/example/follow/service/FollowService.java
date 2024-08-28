package org.example.follow.service;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.follow.domain.dto.FollowDto;
import org.example.follow.domain.entity.Follow;
import org.example.follow.repository.FollowRepository;
import org.example.log.LogExecution;
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
	@LogExecution
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

	@LogExecution
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

	@LogExecution
	public FollowDto.FollowListResponse followingList(String targetUserName) {
		return new FollowDto.FollowListResponse(
			followRepository.findByFromUser(
								this.getUserByNickname(targetUserName)
							).stream()
							.map(Follow::getFromUser)
							.map((following) -> new FollowDto.FollowUser(
								following.getNickname(),
								this.getFollowerCountOfUser(following),
								following.getProfileImageLocationId()
							)).toList()
		);
	}

	@LogExecution
	public FollowDto.FollowListResponse followerList(String targetUserName) {

		return new FollowDto.FollowListResponse(
			followRepository.findByToUser(
								this.getUserByNickname(targetUserName)
							).stream()
							.map(Follow::getToUser)
							.map((follower) -> new FollowDto.FollowUser(
								follower.getNickname(),
								this.getFollowerCountOfUser(follower),
								follower.getProfileImageLocationId()
							)).toList()
		);
	}

	@LogExecution
	private UserEntity getUserByEmail(String email) throws ApiUserException {
		return userRepository.findByEmail(email)
							 .orElseThrow(
								 () -> ApiUserException.builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
									 .build()
							 );
	}

	@LogExecution
	private UserEntity getUserByNickname(String nickname) throws ApiUserException {
		return userRepository.findByNickname(nickname)
							 .orElseThrow(
								 () -> ApiUserException.builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
									 .build()
							 );
	}

	@LogExecution
	private int getFollowerCountOfUser(UserEntity user) {
		return this.followRepository.findByToUser(user).size();
	}
}
