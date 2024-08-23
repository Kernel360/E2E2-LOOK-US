package org.example.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Role {
	ROLE_ADMIN("ADMIN"), ROLE_INFLUENCER("INFLU"), ROLE_USER("USER"), ROLE_GUEST("GUEST");
	private String roleType;

	@JsonCreator
	public static Role forValue(String value) {
		for (Role role : values()) {
			if (role.roleType.equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Invalid role type: " + value);
	}

	@JsonValue
	public String toValue() {
		return this.roleType;
	}
}
