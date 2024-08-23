package org.example.image.imageStorageManager.storage.service.core;

import org.example.image.imageStorageManager.type.StorageFindResult;

public interface StorageService {

	StorageSaveResultInternal save(StoragePacket packet);

	StorageFindResult load(String fileUrl);
}
