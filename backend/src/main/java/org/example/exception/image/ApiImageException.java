package org.example.exception.image;

import java.util.function.Supplier;
import org.example.exception.common.ApiErrorCategory;
import org.example.exception.common.ApiException;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiImageException extends ApiException {

	private final ApiImageErrorSubCategory subCategory;

	@Builder
	protected ApiImageException(
		ApiErrorCategory category,
		ApiImageErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, setErrorData);
		this.subCategory = subCategory;
	}
}
