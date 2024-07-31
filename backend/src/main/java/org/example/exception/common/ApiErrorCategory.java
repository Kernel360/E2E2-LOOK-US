package org.example.exception.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/** ---------------------------------------------------------------
 *     비즈니스 로직에서 발생하는 모든 에러는 ApiErrorCategory로 정의합니다.
 *   - ERROR_TYPE_FOO ( HTTP 상태 코드, "에러 카테고리 대분류" )
 * ----------------------------------------------------------------*/
@Getter
public enum ApiErrorCategory {

	UNIDENTIFIED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "미확인 예외"),
	RESOURCE_INACCESSIBLE(HttpStatus.NOT_FOUND, "리소스 조회 불가"),
	RESOURCE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 사용 권한 제한"),
	;

	private final HttpStatus errorStatusCode;
	private final String errorCategoryName;

	ApiErrorCategory(HttpStatus errorStatusCode, String errorCategoryName) {
		this.errorStatusCode = errorStatusCode;
		this.errorCategoryName = errorCategoryName;
	}

	@Override
	public String toString() {
		return String.format("%s [대분류: %s]",
			this.errorStatusCode,
			this.errorCategoryName
		);
	}
}
