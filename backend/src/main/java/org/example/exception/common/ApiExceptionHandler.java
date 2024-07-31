package org.example.exception.common;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class ApiExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ApiErrorResponse handleException(Exception error) {
		log.error("handleException", error);

		return ApiErrorResponse.from(error);
	}
}
