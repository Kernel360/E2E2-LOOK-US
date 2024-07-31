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
public class PostApiExceptionHandler {

	@ExceptionHandler(PostApiException.class)
	public ApiErrorResponse handleApiException(
		PostApiException postApiError,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		log.error("handlePostApiException", postApiError);

		switch (postApiError.getSubCategory()) {
			case POST_NOT_FOUND:
				// ...
				break;
			default:
				// ...
				break;
		}

		return ApiErrorResponse.from(postApiError);
	}
}

