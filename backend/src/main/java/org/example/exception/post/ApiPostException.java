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
public class ApiPostException extends ApiException {

	@Builder
	protected ApiPostException(
		ApiErrorCategory category,
		ApiPostErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, subCategory, setErrorData);
	}

	@Override
	public void processEachSubCategoryCase() {
		ApiErrorSubCategory subCategory = this.getErrorSubCategory();

		switch ((ApiPostErrorSubCategory)subCategory) {
			case POST_DELETED -> {}
			case POST_DISABLED -> {}
			case POST_NOT_FOUND -> {}
			case POST_INVALID_SCRAP_STATUS -> {}
			default -> {}
		}
	}
}
