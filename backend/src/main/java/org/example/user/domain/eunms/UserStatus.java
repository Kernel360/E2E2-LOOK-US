package org.example.user.domain.eunms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserStatus {
	USER_STATUS_SIGNED_IN("SIGNIN"), USER_STATUS_SIGNED_OUT("SIGNOUT");
	private String userStatusType;
}
