package org.example.image.imageStorageManager.storage.service.core;

import java.nio.file.Path;

import lombok.NonNull;

public record StorageSaveResultInternal(
	@NonNull StorageType storageType,
	@NonNull Path savedPath
) {}
