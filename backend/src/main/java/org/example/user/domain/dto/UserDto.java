package org.example.user.domain.dto;

import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;

public class UserDto {
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
