package org.example.image.storageManager;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.image.ApiImageErrorSubCategory;
import org.example.exception.image.ApiImageException;
import org.example.image.resourceLocation.entity.ResourceLocationEntity;
import org.example.image.resourceLocation.repository.ResourceLocationRepository;
import org.example.image.storage.core.StorageSaveResultInternal;
import org.example.image.storage.strategy.LocalDateDirectoryNamingStrategy;
import org.example.image.storage.core.StorageService;
import org.example.image.storage.core.StoragePacket;
import org.example.image.storage.strategy.UuidV4FileNamingStrategy;
import org.example.image.storageManager.core.StorageFindResult;
import org.example.image.storageManager.core.StorageManager;
import org.example.image.storageManager.core.StorageSaveResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageManager implements StorageManager {

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
	public StorageSaveResult saveResource(MultipartFile file) {

		StoragePacket packet = StoragePacket.builder()
			.fileData(file)
			.fileNamingStrategy( new UuidV4FileNamingStrategy() )
			.directoryNamingStrategy( new LocalDateDirectoryNamingStrategy() )
			.build();

		StorageSaveResultInternal storageSaveResult = this.storageService.save(packet);

		ResourceLocationEntity savedImageLocation = this.imageRepository.save(
			ResourceLocationEntity
				.builder()
				.storageType( storageSaveResult.storageType() )
				.savedPath( storageSaveResult.savedPath().toString() )
				.build()
		);

		return new StorageSaveResult(
			storageSaveResult.storageType(),
			savedImageLocation.getImageLocationId()
		);
	}

	@Override
	public StorageFindResult findResourceById(Long imageId) {
		return this.imageRepository.findById(imageId)
								   .map(
									   image -> this.storageService.load( image.getSavedPath() )
								   )
								   .orElseThrow(() ->
									   ApiImageException.builder()
										   .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
										   .subCategory(ApiImageErrorSubCategory.IMAGE_NOT_FOUND)
										   .build()
								   );
	}
}
