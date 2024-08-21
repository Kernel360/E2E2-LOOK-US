package org.example.image.imageStorageManager.imageStorageManagerImpl;

import java.io.IOException;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.storage.ApiStorageErrorSubCategory;
import org.example.exception.storage.ApiStorageException;
import org.example.image.ImageAnalyzeManager.analyzer.repository.ClothAnalyzeDataRepository;
import org.example.image.ImageAnalyzeManager.analyzer.service.ClothAnalyzeService;
import org.example.image.imageStorageManager.storage.entity.ResourceLocationEntity;
import org.example.image.imageStorageManager.storage.repository.ResourceLocationRepository;
import org.example.image.imageStorageManager.storage.service.core.StoragePacket;
import org.example.image.imageStorageManager.storage.service.core.StorageSaveResultInternal;
import org.example.image.imageStorageManager.storage.service.core.StorageService;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.storage.service.core.strategy.LocalDateDirectoryNamingStrategy;
import org.example.image.imageStorageManager.storage.service.core.strategy.UuidV4FileNamingStrategy;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.type.StorageFindResult;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;

@Service
public class ImageStorageStorageManagerImpl implements ImageStorageManager {

	// 현재는 Local File System 1개만 사용하며, 추후 변경될 예정입니다.
	private final ResourceLocationRepository imageRepository;
	private final StorageService storageService;

	public ImageStorageStorageManagerImpl(
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
	) throws IOException {
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