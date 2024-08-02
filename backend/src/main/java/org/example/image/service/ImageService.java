package org.example.image.service;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.image.ApiImageErrorSubCategory;
import org.example.exception.image.ApiImageException;
import org.example.image.entity.ImageEntity;
import org.example.image.repository.ImageRepository;
import org.example.image.storage.strategy.LocalDateDirectoryNamingStrategy;
import org.example.image.storage.core.StorageService;
import org.example.image.storage.core.StoragePacket;
import org.example.image.storage.core.StorageSaveResult;
import org.example.image.storage.strategy.UuidV4FileNamingStrategy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final StorageService storageService;

	public ImageService(
		ImageRepository imageRepository,
		StorageService storageService
	) {
		this.imageRepository = imageRepository;
		this.storageService = storageService;
	}

	public Long saveImageFile(MultipartFile file) {

		StoragePacket packet = StoragePacket
			.builder()
			.fileData(file)
			.fileNamingStrategy( new UuidV4FileNamingStrategy() )
			.directoryNamingStrategy( new LocalDateDirectoryNamingStrategy() )
			.build();

		StorageSaveResult result = this.storageService.save(packet);

		return this.imageRepository.save(
			ImageEntity.builder()
				.storageType( result.getStorageType() )
				.imageUrl( result.getPath().toString() )
				.build()
		).getImageId();
	}

	public Resource getImageResourceById(Long imageId) {
		return this.imageRepository.findById(imageId)
								   .map(image -> this.storageService.load(image.getImageUrl()))
								   .orElseThrow(() ->
									   ApiImageException.builder()
										   .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
										   .subCategory(ApiImageErrorSubCategory.IMAGE_NOT_FOUND)
										   .build()
								   );
	}

	public String getImageUrlById(Long imageId) {
		return this.imageRepository.findById(imageId)
								   .map(ImageEntity::getImageUrl)
								   .orElseThrow(() ->
									   ApiImageException.builder()
										   .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
										   .subCategory(ApiImageErrorSubCategory.IMAGE_NOT_FOUND)
										   .build()
								   );
	}
}
