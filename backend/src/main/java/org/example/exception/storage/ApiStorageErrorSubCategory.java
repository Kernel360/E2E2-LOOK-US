package org.example.exception.storage;

import org.example.exception.common.ApiErrorSubCategory;

public enum ApiStorageErrorSubCategory implements ApiErrorSubCategory {

	RESOURCE_LOCATION_NOT_FOUND("[ResourceLocationRepository] 리소스의 위치 정보가 존재하지 않습니다."),

	DIRECTORY_NOT_ACCESSIBLE("디렉토리가 존재하지 않습니다."),

	FILE_NOT_FOUND("파일이 존재하지 않습니다."),
	FILE_DUPLICATE("파일이 중복됩니다."),
	FILE_IS_EMPTY("파일이 비어있습니다."),

	FILE_SAVE_PROCESS_FAILURE("파일 저장 작업 실패."),
	FILE_READ_IO_FAILURE("파일 읽기 작업 실패."),
	FILE_NOT_READABLE("파일을 읽을 수 없습니다."),
	// ...
	;

	private final String apiStorageErrorSubCategory;

	ApiStorageErrorSubCategory(String apiStorageErrorSubCategory) {
		this.apiStorageErrorSubCategory = apiStorageErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.apiStorageErrorSubCategory);
	}
}
