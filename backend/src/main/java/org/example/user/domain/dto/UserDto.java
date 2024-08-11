package org.example.user.domain.dto;

import java.time.LocalDateTime;

import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UserDto {
	public record UserCreateRequest(
		String email,
		String password
	) {
	}
	public record UserInfoResponse (
		Long userId,
		String username,
		String email,
		Gender gender,
		String birth,
		String nickname,
		String instaId
	){
		public static UserInfoResponse toDto(UserEntity userEntity) {
			return new UserInfoResponse(
				userEntity.getUserId(),
				userEntity.getUsername(),
				userEntity.getEmail(),
				userEntity.getGender(),
				userEntity.getBirth(),
				userEntity.getNickname(),
				userEntity.getInstaId()
			);
		}
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
}
