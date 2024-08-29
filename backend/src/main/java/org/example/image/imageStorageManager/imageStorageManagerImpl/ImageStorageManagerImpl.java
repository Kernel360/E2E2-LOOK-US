package org.example.image.imageStorageManager.imageStorageManagerImpl;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.storage.ApiStorageErrorSubCategory;
import org.example.exception.storage.ApiStorageException;
import org.example.image.imageStorageManager.storage.entity.ImageLocationEntity;
import org.example.image.imageStorageManager.storage.repository.ImageLocationRepository;
import org.example.image.imageStorageManager.storage.service.core.StoragePacket;
import org.example.image.imageStorageManager.storage.service.core.StorageSaveResultInternal;
import org.example.image.imageStorageManager.storage.service.core.StorageService;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.storage.service.core.strategy.LocalDateDirectoryNamingStrategy;
import org.example.image.imageStorageManager.storage.service.core.strategy.UuidV4FileNamingStrategy;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.example.config.log.LogExecution;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageStorageManagerImpl implements ImageStorageManager {

	// 현재는 Local File System 1개만 사용하며, 추후 변경될 예정입니다.
	private final ImageLocationRepository imageRepository;
	private final StorageService storageService;

	@Override
	@LogExecution
	public StorageSaveResult saveImage(
		@NonNull MultipartFile file,
		StorageType storageType
	) {
		StoragePacket packet = StoragePacket
			.builder()
			.fileData(file)
			.fileNamingStrategy(new UuidV4FileNamingStrategy())
			.directoryNamingStrategy(new LocalDateDirectoryNamingStrategy())
			.build();

		// (1) save image file and store its location path to DB -------------------------
		StorageSaveResultInternal storageSaveResult = switch (storageType) {
			case LOCAL_FILE_SYSTEM -> this.storageService.save(packet);
			// case AWS_S3_STORAGE -> TODO: implementation required
			// case MINIO_STORAGE -> TODO: implementation required
			// case IN_MEMORY -> TODO: implementation required
			default -> throw new AssertionError("[미구현 기능] Unsupported storage type: " + storageType);
		};

		ImageLocationEntity savedImageLocation = this.imageRepository.save(
			ImageLocationEntity
				.builder()
				.storageType(storageSaveResult.storageType())
				.savedPath(storageSaveResult.savedPath().toString())
				.build()
		);

		return new StorageSaveResult(
			storageSaveResult.storageType(),
			savedImageLocation.getImageLocationId()
		);
	}

	@Override
	@LogExecution
	public StorageLoadResult loadImageByLocationId(Long imageLocationId) {
		return this.imageRepository.findById(imageLocationId)
								   .map(
									   image -> this.storageService.load(image.getSavedPath())
								   )
								   .orElseThrow(() ->
									   ApiStorageException
										   .builder()
										   .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
										   .subCategory(ApiStorageErrorSubCategory.RESOURCE_LOCATION_NOT_FOUND)
										   .build()
								   );
	}
}
