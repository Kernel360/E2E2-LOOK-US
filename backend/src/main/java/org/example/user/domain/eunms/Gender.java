package org.example.user.domain.eunms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Gender {
	GENDER_MAN("MAN"), GENDER_WOMAN("WOMAN"), GENDER_NONE("NONE");
	private String genderType;
}
