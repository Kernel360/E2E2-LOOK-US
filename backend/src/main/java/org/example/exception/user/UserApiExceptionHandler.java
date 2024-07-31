package org.example.exception.user;

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
public class UserApiExceptionHandler {

	@ExceptionHandler(UserApiException.class)
	public ApiErrorResponse handleApiException(
		UserApiException userApiError,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		log.error("handleUserApiException", userApiError);

		switch (userApiError.getSubCategory()) {
			case USER_NOT_FOUND:
				// ...
				break;
			case USER_ALREADY_EXISTS:
				// ...
				break;
			case USER_ALREADY_LOGGED_IN:
				// ...
				break;
			default:
				// ...
				break;
		}

		return ApiErrorResponse.from(userApiError);
	}
}

