package org.example.exception.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ApiErrorResponse handleException(Exception error) {
		log.error("handleException", error);

		return ApiErrorResponse.UNKNOWN_ERROR();
	}
}
