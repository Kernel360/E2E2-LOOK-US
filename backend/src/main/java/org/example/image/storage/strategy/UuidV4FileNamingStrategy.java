package org.example.image.storage.strategy;

import java.util.UUID;

public class UuidV4FileNamingStrategy implements FileNamingStrategy {

	@Override
	public String getName() {
		return UUID.randomUUID().toString();
	}
}
