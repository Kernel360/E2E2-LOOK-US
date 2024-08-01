package org.example.exception.common;

import org.springframework.http.ProblemDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

/**
 * Response Error Message Body Format
 * RFC 7807 - "Problem Detail"
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiErrorResponse extends ProblemDetail {

	@JsonProperty(value = "error_data")
	private final Object errorData;

	protected ApiErrorResponse(
		ApiException apiException
	) {
		super(ProblemDetail.forStatusAndDetail(
			apiException.getHttpStatus(),
			apiException.getMessage()
		));
		this.errorData = apiException.getErrorData();
	}

	public static ApiErrorResponse from(ApiException apiException) {
		return new ApiErrorResponse(apiException);
	}

	public static ApiErrorResponse UNIDENTIFIED() {
		return new ApiErrorResponse(ApiException.UNKNOWN_EXCEPTION());
	}
}
