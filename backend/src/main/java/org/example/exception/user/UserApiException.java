package org.example.exception.user;

import java.util.function.Supplier;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.common.ApiErrorSubCategory;
import org.example.exception.common.ApiException;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 기능에 대한 비즈니스 로직 예외 입니다.
 */
@Getter
public class UserApiException extends ApiException {

	@Override
	public UserApiErrorSubCategory getSubCategory() {
		return (UserApiErrorSubCategory)super.getSubCategory();
	}

	@Builder
	protected UserApiException(
		ApiErrorCategory category,
		ApiErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, subCategory, setErrorData);
	}
}