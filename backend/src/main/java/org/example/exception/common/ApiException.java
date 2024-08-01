package org.example.exception.common;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.Builder;

public class ApiException extends RuntimeException {
	private final ApiErrorCategory category;
	private final Supplier<?> errorDataSupplier;

	public static ApiException UNKNOWN_EXCEPTION() {
		return new ApiException(ApiErrorCategory.UNKNOWN_ERROR, null);
	}

	public ApiException(
		ApiErrorCategory category,
		@Nullable Supplier<?> setErrorData
	) {
		super(category.getErrorCategoryName());
		this.category = category;
		this.errorDataSupplier = setErrorData;
	}

	public HttpStatus getHttpStatus() {
		return this.category.getErrorStatusCode();
	}

	public Optional<Object> getErrorData() {
		if (this.errorDataSupplier != null) {
			return Optional.of(this.errorDataSupplier.get());
		}
		return Optional.empty();
	}
}