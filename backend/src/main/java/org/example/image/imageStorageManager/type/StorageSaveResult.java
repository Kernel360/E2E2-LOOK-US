package org.example.image.imageStorageManager.type;

import org.example.image.imageStorageManager.storage.service.core.StorageType;

import lombok.NonNull;

/**
 *
 * @param storageType 저장소 종류 (FileSystem | AmazonS3 | etc ...)
 * @param resourceLocationId 리소스 위치 id
 */
public record StorageSaveResult(
	@NonNull StorageType storageType,
	@NonNull Long resourceLocationId
) {
}
