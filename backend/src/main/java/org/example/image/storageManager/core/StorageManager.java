package org.example.image.storageManager.core;

import org.springframework.web.multipart.MultipartFile;

public interface StorageManager {

	StorageSaveResult saveResource(MultipartFile resource);

	StorageFindResult findResourceById(Long resourceId);
}
