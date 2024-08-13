package org.example.exception.common;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.ToString;

@ToString
public abstract class ApiException extends RuntimeException {
	private final ApiErrorCategory category;
	private final ApiErrorSubCategory subCategory;
	private final Supplier<?> errorDataSupplier;

	public ApiException(
		ApiErrorCategory category,
		@Nullable ApiErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category.getErrorCategoryDescription());
		this.category = category;
		this.subCategory = subCategory;
		this.errorDataSupplier = setErrorData;
	}

	/**
	 * 에러 서브-카테고리를 처리하기 위한 함수입니다.
	 */
	public abstract void processEachSubCategoryCase();

	public HttpStatus getHttpStatus() {
		return this.category.getErrorStatusCode();
	}

	public String getErrorCategoryDescription() {
		return this.category.getErrorCategoryDescription();
	}

	public String getErrorSubCategoryDescription() {
		return this.subCategory.toString();
	}

	public ApiErrorSubCategory getErrorSubCategory() {
		return this.subCategory;
	}

	public ApiErrorCategory getErrorCategory() {
		return this.category;
	}

	public Optional<Object> getErrorData() {
		if (this.errorDataSupplier != null) {
			return Optional.of(this.errorDataSupplier.get());
		}
		return Optional.empty();
	}
}
