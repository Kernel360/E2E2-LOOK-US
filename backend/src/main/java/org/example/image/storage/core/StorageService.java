package org.example.image.storage.core;

import org.example.image.storageManager.core.StorageFindResult;

public interface StorageService {

	StorageSaveResultInternal save(StoragePacket packet);

	StorageFindResult load(String fileUrl);
}
