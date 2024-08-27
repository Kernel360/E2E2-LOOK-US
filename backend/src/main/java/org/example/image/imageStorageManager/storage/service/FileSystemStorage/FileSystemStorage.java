package org.example.image.imageStorageManager.storage.service.FileSystemStorage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.storage.ApiStorageErrorSubCategory;
import org.example.exception.storage.ApiStorageException;
import org.example.image.imageStorageManager.storage.service.core.StoragePacket;
import org.example.image.imageStorageManager.storage.service.core.StorageSaveResultInternal;
import org.example.image.imageStorageManager.storage.service.core.StorageService;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import lombok.NonNull;

/**
 * @link <a href="https://www.baeldung.com/java-images">How to read Images</a>
 * @link <a href="https://spring.io/guides/gs/uploading-files">How to upload files</a>
 */
@Service
public class FileSystemStorage implements StorageService {

	private final Path rootLocation = Paths.get(System.getenv("IMAGE_STORAGE_PATH"));

	@Override
	public StorageSaveResultInternal save(@NonNull StoragePacket packet) {

		if (packet.isPayloadEmpty()) {
			throw ApiStorageException
				.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiStorageErrorSubCategory.FILE_IS_EMPTY)
				.build();
		}

		if (Files.notExists(this.rootLocation)) {
			throw ApiStorageException
				.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiStorageErrorSubCategory.DIRECTORY_NOT_ACCESSIBLE)
				.build();
		}

		try {
			Path destination = this.rootLocation
				.resolve(packet.getDestinationPath())
				.normalize()
				.toAbsolutePath();

			if (Files.notExists(destination.getParent())) {
				this.createDirectory(destination.getParent());
			}

			Files.copy(packet.getFileData().getInputStream(), destination);

			return new StorageSaveResultInternal(
				StorageType.LOCAL_FILE_SYSTEM,
				destination
			);
		} catch (IOException e) {
			throw ApiStorageException
				.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiStorageErrorSubCategory.FILE_SAVE_PROCESS_FAILURE)
				.setErrorData(e::getMessage)
				.build();
		}
	}

	@Override
	public StorageLoadResult load(@NonNull String filePath) {
		try {
			Path fullPath = this.rootLocation.resolve(filePath).normalize().toAbsolutePath();
			Resource resource = new UrlResource(fullPath.toUri());

			if ((false == resource.exists()) || (false == resource.isReadable())) {
				throw ApiStorageException
					.builder()
					.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
					.subCategory(ApiStorageErrorSubCategory.FILE_NOT_READABLE)
					.build();
			}

			return new StorageLoadResult(
				StorageType.LOCAL_FILE_SYSTEM,
				resource
			);
		} catch (MalformedURLException e) {
			throw ApiStorageException
				.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiStorageErrorSubCategory.FILE_READ_IO_FAILURE)
				.build();
		}
	}

	private void createDirectory(@NonNull Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw ApiStorageException
				.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiStorageErrorSubCategory.DIRECTORY_NOT_ACCESSIBLE)
				.build();
		}
	}
}
