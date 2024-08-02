package org.example.exception.image;

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
public class ApiImageExceptionHandler {

	@ExceptionHandler(ApiImageException.class)
	public ApiErrorResponse handleApiException(
		ApiImageException apiImageError,
		HttpServletRequest request,
		HttpServletResponse response
	) {

		log.error("handleApiImageException", apiImageError);

		switch (apiImageError.getSubCategory()) {
			case IMAGE_NOT_FOUND:
				break;
			default:
				break;
		}

		return ApiErrorResponse.from(apiImageError);
	}
}
