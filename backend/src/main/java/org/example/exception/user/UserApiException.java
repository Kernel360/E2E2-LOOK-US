package org.example.exception.user;

import java.util.function.Supplier;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.common.ApiException;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 기능에 대한 비즈니스 로직 예외 입니다.
 */
@Getter
public class UserApiException extends ApiException {

	private final UserApiErrorSubCategory subCategory;

	@Builder
	protected UserApiException(
		ApiErrorCategory category,
		UserApiErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, setErrorData);
		this.subCategory = subCategory;
	}
}