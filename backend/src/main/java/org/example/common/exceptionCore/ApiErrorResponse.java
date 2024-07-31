package org.example.common.exceptionCore;

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
	private final Object generatedErrorBody;

	protected ApiErrorResponse(ApiException apiException) {
		super(ProblemDetail.forStatusAndDetail(
			apiException.getHttpStatus(),
			apiException.getMessage()
		));
		this.generatedErrorBody = apiException.generateErrorBody();
	}

	public static ApiErrorResponse from(ApiException apiException) {
		return new ApiErrorResponse(apiException);
	}
}
