package org.example.image.imageStorageManager.storage.service.core.strategy;

import java.util.UUID;

public class UuidV4FileNamingStrategy implements FileNamingStrategy {

	@Override
	public String getName() {
		return UUID.randomUUID().toString();
	}
}
