package org.example.exception.storage;

import org.example.exception.common.ApiErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(1)
public class ApiStorageExceptionHandler {

	@ExceptionHandler(ApiStorageException.class)
	public ApiErrorResponse handleApiException(
		ApiStorageException ApiStorageError,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		log.error("handleStorageApiException", ApiStorageError);

		switch (ApiStorageError.getSubCategory()) {
			case FILE_NOT_FOUND:
				break;
			default:
				break;
		}

		return ApiErrorResponse.from(ApiStorageError);
	}
}

