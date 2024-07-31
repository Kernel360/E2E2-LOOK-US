package org.example.exception.common;

import java.util.Optional;
import java.util.function.Supplier;

import org.example.exception.user.UserApiErrorSubCategory;
import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.Builder;

public abstract class ApiException extends RuntimeException {
	private final ApiErrorCategory category;
	private final Supplier<?> errorDataSupplier;

	@Builder
	protected ApiException(
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