package org.example.user.domain.eunms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Role {
	ROLE_ADMIN("ADMIN"), ROLE_INFLUENCER("INFLU"), ROLE_USER("USER"), ROLE_GUEST("GUEST");
	private String roleType;
}
