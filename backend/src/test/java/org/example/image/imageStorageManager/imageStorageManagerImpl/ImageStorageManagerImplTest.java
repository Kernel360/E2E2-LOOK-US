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
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ImageStorageManagerImplTest {

	@InjectMocks
	private ImageStorageManagerImpl imageStorageManager;

	@Mock
	private ImageLocationRepository imageRepository;

	@Mock
	private StorageService storageService;

	@Mock
	private MultipartFile file;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveImage() {
		// Arrange
		Path mockPath = Path.of("/mock/path");
		StorageSaveResultInternal storageSaveResultInternal = new StorageSaveResultInternal(
			StorageType.LOCAL_FILE_SYSTEM,
			mockPath
		);

		ImageLocationEntity mockEntity = mock(ImageLocationEntity.class);

		// Mock MultipartFile behavior
		when(file.getOriginalFilename()).thenReturn("test_image.jpg");
		when(storageService.save(any(StoragePacket.class))).thenReturn(storageSaveResultInternal);
		when(imageRepository.save(any(ImageLocationEntity.class))).thenReturn(mockEntity);

		// Act
		StorageSaveResult result = imageStorageManager.saveImage(file, StorageType.LOCAL_FILE_SYSTEM);

		// Assert
		assertNotNull(result);
		assertEquals(StorageType.LOCAL_FILE_SYSTEM, result.storageType());

	}

	@Test
	void testLoadImageByLocationId() {
		// Arrange
		Path mockPath = Path.of("/mock/path");
		ImageLocationEntity mockEntity = ImageLocationEntity.builder()
			.savedPath(mockPath.toString())
			.build();

		// Mocking StorageLoadResult
		Resource mockResource = mock(Resource.class);
		when(mockResource.exists()).thenReturn(true);

		StorageLoadResult mockLoadResult = new StorageLoadResult(StorageType.LOCAL_FILE_SYSTEM, mockResource);

		when(imageRepository.findById(anyLong())).thenReturn(Optional.of(mockEntity));
		when(storageService.load(anyString())).thenReturn(mockLoadResult);

		// Act
		StorageLoadResult result = imageStorageManager.loadImageByLocationId(anyLong());

		// Assert
		assertNotNull(result);
		assertTrue(result.resource().exists());
	}


	@Test
	void testLoadImageByLocationIdThrowsException() {
		// Arrange
		when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Act & Assert
		ApiStorageException exception = assertThrows(ApiStorageException.class, () ->
			imageStorageManager.loadImageByLocationId(1L)
		);

		assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
		assertEquals(ApiStorageErrorSubCategory.RESOURCE_LOCATION_NOT_FOUND, exception.getErrorSubCategory());
	}
}
