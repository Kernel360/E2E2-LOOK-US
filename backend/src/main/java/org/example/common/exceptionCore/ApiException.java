package org.example.common.exceptionCore;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.Builder;

public class ApiException extends RuntimeException {
	private final ApiErrorCategory apiErrorCategory;
	private final ErrorDetailSupplier errorDetailSupplier;

	@Builder
	protected ApiException(
		ApiErrorCategory category,
		String detail,
		@Nullable ErrorDetailSupplier setErrorData
	) {
		super(detail);
		this.apiErrorCategory = category;
		this.errorDetailSupplier = setErrorData;
	}

	public HttpStatus getHttpStatus() {
		return this.apiErrorCategory.getErrorStatusCode();
	}

	public Optional<Object> generateErrorBody() {
		if (this.errorDetailSupplier != null) {
			return Optional.of(this.errorDetailSupplier.get());
		}
		return Optional.empty();
	}
}