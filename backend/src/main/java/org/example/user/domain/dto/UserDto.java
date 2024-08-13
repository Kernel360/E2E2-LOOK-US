package org.example.user.domain.dto;

import java.util.List;

import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;

import com.querydsl.core.annotations.QueryProjection;

public class UserDto {
	public record UserCreateRequest(
		String email,
		String password
	) {
	}

	public record UserUpdateRequest(
		String birth,
		String instaId,
		String nickName,
		Gender gender
	) {
	}

	public record UserUpdateResponse(
		Long userId
	) {
		public static UserUpdateResponse toDto(UserEntity userEntity) {
			return new UserUpdateResponse(
				userEntity.getUserId()
			);
		}
	}

	public record UserGetInfoResponse(
		String username,
		String email,
		Gender gender,
		String birth,
		String nickname,
		String instaId,
		int postNum

	) {

		public static UserGetInfoResponse toDto(UserEntity userEntity, Integer postNum) {
			return new UserGetInfoResponse(
				userEntity.getUsername(),
				userEntity.getEmail(),
				userEntity.getGender(),
				userEntity.getBirth(),
				userEntity.getNickname(),
				userEntity.getInstaId(),
				postNum
			);
		}

	}

	public record UserGetPostsResponse(
		Long imageId,
		String postContent,
		List<String> hashtags,
		Integer likeCount) {

		@QueryProjection
		public UserGetPostsResponse(
			Long imageId,
			String postContent,
			List<String> hashtags,
			Integer likeCount
		) {
			this.imageId = imageId;
			this.postContent = postContent;
			this.hashtags = hashtags;
			this.likeCount = likeCount;
		}
	}
}
