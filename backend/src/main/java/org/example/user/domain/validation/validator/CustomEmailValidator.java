package org.example.user.domain.validation.validator;

import java.util.regex.Pattern;

import org.example.user.domain.validation.annotation.CustomEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomEmailValidator implements ConstraintValidator<CustomEmail, String> {
	private String regexp;

	@Override
	public void initialize(CustomEmail constraintAnnotation) {
		this.regexp = constraintAnnotation.regexp();
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return Pattern.matches(regexp, email);
	}
}
