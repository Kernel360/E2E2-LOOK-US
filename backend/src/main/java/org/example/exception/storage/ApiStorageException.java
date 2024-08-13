package org.example.exception.storage;

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
public class ApiStorageException extends ApiException {

	@Builder
	protected ApiStorageException(
		ApiErrorCategory category,
		ApiStorageErrorSubCategory subCategory,
		@Nullable Supplier<?> setErrorData
	) {
		super(category, subCategory, setErrorData);
	}

	@Override
	public void processEachSubCategoryCase() {
		ApiErrorSubCategory subCategory = this.getErrorSubCategory();

		switch ( (ApiStorageErrorSubCategory)subCategory ) {
			case FILE_IS_EMPTY -> {}
			case FILE_NOT_FOUND -> {}
			case FILE_NOT_READABLE -> {}
			case FILE_DUPLICATE -> {}
			case FILE_READ_IO_FAILURE -> {}
			case DIRECTORY_NOT_ACCESSIBLE -> {}
			case RESOURCE_LOCATION_NOT_FOUND -> {}
			case FILE_SAVE_PROCESS_FAILURE -> {}
			default -> {}
		}
	}
}
