package org.example.image.storageManager.imageStorageManager;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.storage.ApiStorageErrorSubCategory;
import org.example.exception.storage.ApiStorageException;
import org.example.image.resourceLocation.entity.ResourceLocationEntity;
import org.example.image.resourceLocation.repository.ResourceLocationRepository;
import org.example.image.storage.core.StoragePacket;
import org.example.image.storage.core.StorageSaveResultInternal;
import org.example.image.storage.core.StorageService;
import org.example.image.storage.core.StorageType;
import org.example.image.storage.strategy.LocalDateDirectoryNamingStrategy;
import org.example.image.storage.strategy.UuidV4FileNamingStrategy;
import org.example.image.storageManager.StorageManager;
import org.example.image.storageManager.common.StorageFindResult;
import org.example.image.storageManager.common.StorageSaveResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;

@Service
public class ImageStorageManager implements StorageManager {
	// TODO: 현재는 Local File System 1개만 사용하며, 추후 변경될 예정입니다.

	private final ResourceLocationRepository imageRepository;
	private final StorageService storageService;

	public ImageStorageManager(
		ResourceLocationRepository resourceLocationRepository,
		StorageService storageService
	) {
		this.imageRepository = resourceLocationRepository;
		this.storageService = storageService;
	}

	@Override
	public StorageSaveResult saveResource(
		@NonNull MultipartFile file,
		StorageType storageType
	) {
		StoragePacket packet = StoragePacket
			.builder()
			.fileData(file)
			.fileNamingStrategy(new UuidV4FileNamingStrategy())
			.directoryNamingStrategy(new LocalDateDirectoryNamingStrategy())
			.build();

		StorageSaveResultInternal storageSaveResult = switch (storageType) {
			case LOCAL_FILE_SYSTEM -> this.storageService.save(packet);
			// case AWS_S3_STORAGE -> TODO: implementation required
			// case MINIO_STORAGE -> TODO: implementation required
			// case IN_MEMORY -> TODO: implementation required
			default -> throw new AssertionError("[미구현 기능] Unsupported storage type: " + storageType);
		};

		ResourceLocationEntity savedImageLocation = this.imageRepository.save(
			ResourceLocationEntity
				.builder()
				.storageType(storageSaveResult.storageType())
				.savedPath(storageSaveResult.savedPath().toString())
				.build()
		);

		return new StorageSaveResult(
			storageSaveResult.storageType(),
			savedImageLocation.getResourceLocationId()
		);
	}

	@Override
	public StorageFindResult findResourceById(Long imageId) {
		return this.imageRepository.findById(imageId)
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
