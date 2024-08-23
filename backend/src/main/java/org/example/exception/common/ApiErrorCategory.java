package org.example.exception.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/** ---------------------------------------------------------------
 *     비즈니스 로직에서 발생하는 모든 에러는 ApiErrorCategory로 정의합니다.
 *   - ERROR_TYPE_FOO ( HTTP 상태 코드, "에러 카테고리 대분류" )
 * ----------------------------------------------------------------*/
@Getter
public enum ApiErrorCategory {

	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "미확인 예외"),

	RESOURCE_INACCESSIBLE(HttpStatus.NOT_FOUND, "리소스 조회 불가"),

	RESOURCE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 사용 권한 제한"),

	RESOURCE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 형식"),
	;


	private final HttpStatus errorStatusCode;
	private final String errorCategoryDescription;

	ApiErrorCategory(HttpStatus errorStatusCode, String errorCategoryDescription) {
		this.errorStatusCode = errorStatusCode;
		this.errorCategoryDescription = errorCategoryDescription;
	}

	@Override
	public String toString() {
		return String.format("%s [분류: %s]",
			this.errorStatusCode,
			this.errorCategoryDescription
		);
	}
}
