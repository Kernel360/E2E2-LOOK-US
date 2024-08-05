package org.example.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Gender {
	GENDER_MAN("MAN"), GENDER_WOMAN("WOMAN"), GENDER_NONE("NONE");
	private String genderType;

	@JsonCreator
	public static Gender forValue(String value) {
		for (Gender gender : values()) {
			if (gender.genderType.equalsIgnoreCase(value)) {
				return gender;
			}
		}
		throw new IllegalArgumentException("Invalid gender type: " + value);
	}

	@JsonValue
	public String toValue() {
		return this.genderType;
	}
}
