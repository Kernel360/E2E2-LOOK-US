package org.example;

public enum PostStatus {
	PUBLISHED("PUBLISHED"),
	DISABLED("DISABLED"),
	;
	private String postStatusType;

	PostStatus(String published) {
		this.postStatusType = published;
	}
}