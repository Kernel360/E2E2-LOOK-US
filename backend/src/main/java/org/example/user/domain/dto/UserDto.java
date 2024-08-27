package org.example.user.domain.dto;

import java.util.List;

import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;

import com.querydsl.core.annotations.QueryProjection;

public class UserDto {

	// 회원 가입 요청 DTO
	public record UserCreateRequest(
		String email,
		String password
	) {
	}

	// 회원 정보 수정 요청 DTO
	public record UserUpdateRequest(
		String birth,
		String instaId,
		String nickName,
		Gender gender
	) {
	}

	// 회원 정보 수정 응답 DTO
	public record UserUpdateResponse(
		Long userId
	) {
		public static UserUpdateResponse toDto(UserEntity userEntity) {
			return new UserUpdateResponse(
				userEntity.getUserId()
			);
		}
	}

	// 회원 정보 조회 응답 DTO
	public record UserGetInfoResponse(
		String username,
		String email,
		Gender gender,
		String birth,
		String nickname,
		String instaId,
		Long imageLocationId,
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
				userEntity.getProfileImageLocationId(),
				postNum
			);
		}
	}

	// 회원이 작성한 게시글 조회 응답 DTO
	public record UserGetPostsResponse(
		Long imageLocationId,
		String postContent,
		List<String> hashtags,
		Integer likeCount,
		Long postId
	) {
		@QueryProjection
		public UserGetPostsResponse(
			Long imageLocationId,
			String postContent,
			List<String> hashtags,
			Integer likeCount,
			Long postId
		) {
			this.imageLocationId = imageLocationId;
			this.postContent = postContent;
			this.hashtags = hashtags;
			this.likeCount = likeCount;
			this.postId = postId;
		}
	}

	// 로그인 요청 DTO
	public record UserLoginRequest(
		String email,
		String password
	) {
	}

	// 사용자 정보 응답 DTO
	public record UserResponse(
		String email,
		String role
	) {
		public static UserResponse fromEntity(UserEntity userEntity) {
			return new UserResponse(
				userEntity.getEmail(),
				userEntity.getRole().name()
			);
		}
	}
}
