package org.example.exception.post;

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
public class ApiPostExceptionHandler {

	@ExceptionHandler(ApiPostException.class)
	public ApiErrorResponse handleApiException(
		ApiPostException apiPostError,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		log.error("handlePostApiException", apiPostError);

		switch (apiPostError.getSubCategory()) {
			case POST_NOT_FOUND:
				break;
			case POST_DISABLED:
				break;
			default:
				break;
		}

		return ApiErrorResponse.from(apiPostError);
	}
}

