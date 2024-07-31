package org.example.exception.post;

import java.util.function.Supplier;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.common.ApiErrorSubCategory;
import org.example.exception.common.ApiException;
import org.example.exception.user.UserApiErrorSubCategory;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 기능에 대한 비즈니스 로직 예외 입니다.
 */
@Getter
public class PostApiException extends ApiException {

	private final PostApiErrorSubCategory subCategory;

	@Builder
	protected PostApiException(
		ApiErrorCategory category,
		PostApiErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, setErrorData);
		this.subCategory = subCategory;
	}
}