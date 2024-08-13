package org.example.user.domain.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum UserStatus {
	USER_STATUS_ACTIVATE("ACTIVATE"), USER_STATUS_DEACTIVATE("DEACTIVATE");
	private String userStatusType;

	UserStatus(String userSign) {
		this.userStatusType = userSign;
	}
}
