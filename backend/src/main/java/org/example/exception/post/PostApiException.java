package org.example.exception.post;

import java.util.function.Supplier;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.common.ApiErrorSubCategory;
import org.example.exception.common.ApiException;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 기능에 대한 비즈니스 로직 예외 입니다.
 */
@Getter
public class PostApiException extends ApiException {

	@Override
	public PostApiErrorSubCategory getSubCategory() {
		return (PostApiErrorSubCategory)super.getSubCategory();
	}

	@Builder
	protected PostApiException(
		ApiErrorCategory category,
		ApiErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, subCategory, setErrorData);
	}
}