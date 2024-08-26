package org.example.post.repository.custom;

import lombok.Getter;

@Getter
public enum UpdateScoreType {

	VIEW(1),        // view : +1
	LIKE(3),        // like : +3, cancel : -3
	LIKE_CANCEL(-3),
	SCRAP(5),        // scrap : +5, cancel : -5
	SCRAP_CANCEL(-5);

	private final int value;

	UpdateScoreType(int value) {
		this.value = value;
	}
}
