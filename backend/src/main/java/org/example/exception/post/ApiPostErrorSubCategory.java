package org.example.exception.post;

public enum ApiPostErrorSubCategory {

	POST_NOT_FOUND("존재하지 않는 포스트입니다."),
	POST_DISABLED("포스트가 비활성화 되어 있습니다"),
	POST_DELETED("포스트가 이미 제거되었습니다.")
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
