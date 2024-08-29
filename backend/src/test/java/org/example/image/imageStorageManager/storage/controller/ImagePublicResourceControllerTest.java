package org.example.image.imageStorageManager.storage.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ImagePublicResourceControllerTest {
	// Serve file successfully when valid imageLocationId is provided
	@Test
	public void test_serve_file_successfully_with_valid_imageLocationId() {
		// Arrange
		Long validImageLocationId = 1L;
		Resource mockResource = Mockito.mock(Resource.class);
		Mockito.when(mockResource.getFilename()).thenReturn("testImage.jpg");
		StorageLoadResult mockResult = new StorageLoadResult(StorageType.LOCAL_FILE_SYSTEM, mockResource);
		ImageStorageManager mockManager = Mockito.mock(ImageStorageManager.class);
		Mockito.when(mockManager.loadImageByLocationId(validImageLocationId)).thenReturn(mockResult);
		ImagePublicResourceController controller = new ImagePublicResourceController(mockManager);

		// Act
		ResponseEntity<Resource> response = controller.serveFile(validImageLocationId);

		// Assert
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals("attachment; filename=\"testImage.jpg\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
		Assertions.assertEquals(mockResource, response.getBody());
	}
}