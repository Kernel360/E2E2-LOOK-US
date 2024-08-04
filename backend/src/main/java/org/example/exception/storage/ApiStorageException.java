package org.example.exception.storage;

import java.util.function.Supplier;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.common.ApiException;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 기능에 대한 비즈니스 로직 예외 입니다.
 */
@Getter
public class ApiStorageException extends ApiException {

	private final ApiStorageErrorSubCategory subCategory;

	@Builder
	protected ApiStorageException(
		ApiErrorCategory category,
		ApiStorageErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, setErrorData);
		this.subCategory = subCategory;
	}
}