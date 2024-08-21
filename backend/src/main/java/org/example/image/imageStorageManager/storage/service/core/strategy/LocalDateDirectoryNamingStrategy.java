package org.example.image.imageStorageManager.storage.service.core.strategy;

import java.time.LocalDate;

public class LocalDateDirectoryNamingStrategy implements DirectoryNamingStrategy {

	@Override
	public String getDirectory() {
		return LocalDate.now().toString();
	};
}
