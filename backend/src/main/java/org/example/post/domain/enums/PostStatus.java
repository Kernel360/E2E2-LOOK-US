package org.example.post.domain.enums;

public enum PostStatus {

	PUBLISHED("PUBLISHED"),
	DISABLED("DISABLED");

	private final String postStatusType;

	PostStatus(String published) {
		this.postStatusType = published;
	}
}
