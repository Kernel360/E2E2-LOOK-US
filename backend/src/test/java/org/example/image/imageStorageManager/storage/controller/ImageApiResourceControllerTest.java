package org.example.image.imageStorageManager.storage.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

class ImageApiResourceControllerTest {

	// Uploading a valid image file returns a 201 status code
	@Test
	public void test_uploading_valid_image_returns_201() throws IOException {
		// Arrange
		MultipartFile mockFile = Mockito.mock(MultipartFile.class);
		ImageStorageManager mockManager = Mockito.mock(ImageStorageManager.class);
		StorageSaveResult mockResult = new StorageSaveResult(StorageType.LOCAL_FILE_SYSTEM, 1L);
		Mockito.when(mockManager.saveImage(mockFile, StorageType.LOCAL_FILE_SYSTEM)).thenReturn(mockResult);
		ImageApiResourceController controller = new ImageApiResourceController(mockManager);

		// Act
		ResponseEntity<Long> response = controller.handleImageUpload(mockFile);

		// Assert
		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assertions.assertEquals(1L, response.getBody());
	}
}