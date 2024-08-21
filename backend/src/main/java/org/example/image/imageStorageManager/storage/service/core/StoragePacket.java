package org.example.image.imageStorageManager.storage.service.core;

import java.nio.file.Path;

import org.example.image.imageStorageManager.storage.service.core.strategy.DirectoryNamingStrategy;
import org.example.image.imageStorageManager.storage.service.core.strategy.FileNamingStrategy;
import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.NonNull;

public class StoragePacket {

	private final MultipartFile fileData;

	private final FileNamingStrategy fileNamingStrategy;

	private final DirectoryNamingStrategy directoryNamingStrategy;

	private String directoryName;

	private String fileName;

	private String extension;

	public MultipartFile getFileData() {
		return this.fileData;
	}

	public Path getDestinationPath() {
		return Path.of(
			new StringBuilder()
				.append( this.directoryName )
				.append( "/" )
				.append( this.fileName )
				.toString()
		);
	}

	public boolean isPayloadEmpty() {
		return this.fileData.isEmpty();
	}

	@Builder
	public StoragePacket(
		@NonNull MultipartFile fileData,
		@NonNull FileNamingStrategy fileNamingStrategy,
		@NonNull DirectoryNamingStrategy directoryNamingStrategy
	) {
		this.fileNamingStrategy = fileNamingStrategy;
		this.directoryNamingStrategy = directoryNamingStrategy;

		this.fileData = fileData;
		this.extension = this.extractFileExtension(fileData);

		if ( fileData.getOriginalFilename() != null ) {

			this.directoryName = this.directoryNamingStrategy.getDirectory();
			this.fileName = this.fileNamingStrategy.getName() + "." + this.extension;
		}
	}

	private String extractFileExtension(MultipartFile file) {
		int dotPos = file.getOriginalFilename().lastIndexOf(".");
		return file.getOriginalFilename().substring(dotPos+1);
	}
}
