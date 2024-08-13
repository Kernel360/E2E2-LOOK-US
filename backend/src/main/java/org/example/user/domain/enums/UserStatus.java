package org.example.user.domain.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum UserStatus {
	USER_STATUS_ACTIVATE("SIGNIN"), USER_STATUS_DEACTIVATE("SIGNOUT");
	private String userStatusType;

	UserStatus(String userSign) {
		this.userStatusType = userSign;
	}
}
