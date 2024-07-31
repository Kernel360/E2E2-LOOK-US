package org.example.common.exceptionCore;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ApiErrorCategory {
	/** ---------------------------------------------------------------
	 *     비즈니스 로직에서 발생하는 모든 에러는 ApiErrorCategory로 정의합니다.
	 *   - API_ERROR_SYMBOL_EXAMPLE ( HTTP 상태 코드, "에러 카테고리" )
	 * ----------------------------------------------------------------*/
	INVALID_BOARD(HttpStatus.BAD_REQUEST, "게시판 카테고리 오류"),
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시판 조회 불가"),
	ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 조회 불가"),
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글 조회 불가"),
	UNAUTHORIZED_VIEWER(HttpStatus.UNAUTHORIZED, "권한 없음"),
	;

	private final HttpStatus errorStatusCode;
	private final String errorCategoryName;

	ApiErrorCategory(HttpStatus errorStatusCode, String errorCategoryName) {
		this.errorStatusCode = errorStatusCode;
		this.errorCategoryName = errorCategoryName;
	}

	@Override
	public String toString() {
		return String.format("%s [분류: %s]",
			this.errorStatusCode,
			this.errorCategoryName
		);
	}
}
