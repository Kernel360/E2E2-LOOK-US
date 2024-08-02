package org.example.image.storage.core;

import java.nio.file.Path;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class StorageSaveResult {

	private Path path;
	private StorageType storageType;

	public StorageSaveResult(
		@NonNull Path path,
		@NonNull StorageType storageType
	) {
		this.path = path;
		this.storageType = storageType;
	}
}
