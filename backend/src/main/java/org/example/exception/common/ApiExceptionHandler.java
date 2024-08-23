package org.example.exception.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ApiErrorResponse handleApiException(ApiException exception) {
		log.error(exception.toString(), exception);
		exception.processEachSubCategoryCase();

		return ApiErrorResponse.from(exception);
	}

	@ExceptionHandler(Exception.class)
	public ApiErrorResponse handleGlobalException(Exception exception) {
		log.error("handleException", exception);

		return ApiErrorResponse.UNKNOWN_ERROR(exception);
	}
}
