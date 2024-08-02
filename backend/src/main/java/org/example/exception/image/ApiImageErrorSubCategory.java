package org.example.exception.image;

public enum ApiImageErrorSubCategory {

	IMAGE_NOT_FOUND("ImageId에 매칭되는 이미지가 없습니다.")
	;

	private final String apiImageErrorSubCategory;

	ApiImageErrorSubCategory(String apiImageErrorSubCategory) {
		this.apiImageErrorSubCategory = apiImageErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.apiImageErrorSubCategory);
	}
}
