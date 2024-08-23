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

	protected ApiErrorResponse(ApiException apiException) {
		super( ProblemDetail.forStatus(apiException.getHttpStatus()) );
		super.setProperty("data", apiException.getErrorData());
		super.setProperty("category", apiException.getMessage());
		super.setProperty("subcategory", apiException.getErrorSubCategoryDescription());
	}

	private ApiErrorResponse(Exception exception) {
		super(ProblemDetail.forStatus(
			ApiErrorCategory.UNKNOWN_ERROR.getErrorStatusCode()
		));
		super.setProperty("category",
			ApiErrorCategory.UNKNOWN_ERROR.getErrorCategoryDescription()
		);
		// TODO: 예외 메시지가 클라이언트에 노출됩니다.
		//       따라서 배포 버전에서는 아래 힌트가 비활성화 되어야 합니다.
		super.setProperty("debug-hint", exception.getMessage());
	}

	public static ApiErrorResponse from(ApiException apiException) {
		return new ApiErrorResponse(apiException);
	}

	public static ApiErrorResponse UNKNOWN_ERROR(Exception exception) {
		return new ApiErrorResponse(exception);
	}
}
