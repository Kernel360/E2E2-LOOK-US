package org.example.image.storage.core;

import org.springframework.core.io.Resource;

public interface StorageService {

	StorageSaveResult save(StoragePacket packet);

	Resource load(String fileUrl);
}