package org.example.exception.user;

import org.example.exception.common.ApiErrorSubCategory;

public enum ApiUserErrorSubCategory implements ApiErrorSubCategory {

	USER_NOT_FOUND("존재하지 않는 사용자입니다."),

	USER_ALREADY_EXISTS("이미 존재하는 사용자입니다."),

	USER_ALREADY_LOGGED_IN("이미 로그인한 사용자입니다."),

	USER_SCRAP_DUPLICATION("리소스 스크랩은 리소스 당 1번만 가능합니다."),

	USER_DEACTIVATE("회원 탈퇴한 유저입니다."),

	USER_UPDATE_IMPOSSIBLE("업데이트 할 회원 정보가 입력되지 않았습니다."),

	USER_FOLLOW_INVALID_REQUEST("비정상적인 팔로우 요청입니다."),

	USER_INVALID_REFRESH_TOKEN("Refresh 토큰 검증에 실패하였습니다."),

	USER_REFRESH_TOKEN_NOT_FOUND("Refresh 토큰 정보가 존재하지 않습니다."),
	;

	private final String userApiErrorSubCategory;

	ApiUserErrorSubCategory(String userApiErrorSubCategory) {
		this.userApiErrorSubCategory = userApiErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.userApiErrorSubCategory);
	}
}
