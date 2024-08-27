package org.example.image.imageStorageManager.storage.service.core;

import org.example.image.imageStorageManager.type.StorageLoadResult;

public interface StorageService {

	StorageSaveResultInternal save(StoragePacket packet);

	StorageLoadResult load(String fileUrl);
}
