/*
package org.example.image.ImageAnalyzeManager.ImageAnalyzeManagerImpl;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.entity.ClothAnalyzeDataEntity;
import org.example.image.ImageAnalyzeManager.analyzer.repository.ClothAnalyzeDataRepository;
import org.example.image.ImageAnalyzeManager.analyzer.service.ImageAnalyzeVisionService;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

class ImageAnalyzeManagerImplTest {

	@Test
	public void test_analyze_successfully_processes_and_saves_data() throws IOException {
		// Arrange
		Long imageLocationId = 1L;
		byte[] imageBytes = new byte[] {1, 2, 3};

		// Mock StorageLoadResult
		StorageLoadResult storageLoadResult = mock(StorageLoadResult.class);
		ByteArrayResource byteArrayResource = mock(ByteArrayResource.class);
		InputStream inputStream = new ByteArrayInputStream(imageBytes);

		// Setup mocks
		when(storageLoadResult.resource()).thenReturn(byteArrayResource);
		when(byteArrayResource.getInputStream()).thenReturn(inputStream);

		ClothAnalyzeData clothAnalyzeData = ClothAnalyzeData.builder()
			.clothType(ClothType.SKIRT)
			.clothName("Shirt")
			.rgbColor(new RGBColor(255, 0, 0))
			.labColor(new LabColor(50F, 50F, 50F))
			.leftTopVertex(new NormalizedVertex2D(0.1f, 0.1f))
			.rightBottomVertex(new NormalizedVertex2D(0.9f, 0.9f))
			.tristimulus(new float[] {1.0f, 2.0f, 3.0f})
			.build();

		ImageStorageManager imageStorageManager = mock(ImageStorageManager.class);
		ImageAnalyzeVisionService imageAnalyzeVisionService = mock(ImageAnalyzeVisionService.class);
		ClothAnalyzeDataRepository clothAnalyzeDataRepository = mock(ClothAnalyzeDataRepository.class);

		when(imageStorageManager.loadImageByLocationId(imageLocationId)).thenReturn(storageLoadResult);
		when(imageAnalyzeVisionService.analyzeImage(imageBytes)).thenReturn(List.of(clothAnalyzeData));

		ImageAnalyzeManagerImpl imageAnalyzeManager = new ImageAnalyzeManagerImpl(imageAnalyzeVisionService,
			clothAnalyzeDataRepository, imageStorageManager);

		// Act
		imageAnalyzeManager.analyze(imageLocationId);

		// Assert
		verify(clothAnalyzeDataRepository, times(1)).save(any(ClothAnalyzeDataEntity.class));
	}
}
*/
