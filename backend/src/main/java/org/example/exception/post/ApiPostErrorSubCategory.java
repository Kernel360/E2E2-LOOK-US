package org.example.exception.post;

import org.example.exception.common.ApiErrorSubCategory;

public enum ApiPostErrorSubCategory implements ApiErrorSubCategory {

	POST_NOT_FOUND("존재하지 않는 포스트입니다."),

	POST_DISABLED("포스트가 비활성화 되어 있습니다"),

	POST_DELETED("포스트가 이미 제거되었습니다."),

	POST_INVALID_SCRAP_STATUS("비정상적인 게시글 스크랩 요청입니다."),

	POST_INVALID_AUTHOR("게시글 작성자가 아닙니다."),

	POST_INVALID_UPDATE("비정상적인 게시글 수정 요청입니다."),

	POST_INVALID_LIKE_REQUEST("비정상적인 게시글 좋아요 요청입니다."),
	;

	private final String apiPostErrorSubCategory;

	ApiPostErrorSubCategory(String apiPostErrorSubCategory) {
		this.apiPostErrorSubCategory = apiPostErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.apiPostErrorSubCategory);
	}
}
