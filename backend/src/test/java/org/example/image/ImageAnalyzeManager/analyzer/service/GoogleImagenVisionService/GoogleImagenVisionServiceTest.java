/*
package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.type.GoogleImagenVisionDto;
import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils.GoogleApiCredentialRoller;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class GoogleImagenVisionServiceTest {

	@Test
	public void testAnalyzeImage() throws IOException {
		// Setup mock dependencies
		GoogleApiCredentialRoller apiKeyRoller = mock(GoogleApiCredentialRoller.class);
		ImageAnnotatorClient mockClient = mock(ImageAnnotatorClient.class);

		// Mock behavior for GoogleApiCredentialRoller
		when(apiKeyRoller.getCredential()).thenReturn(null);

		// Mock behavior for ImageAnnotatorClient
		ByteString imgBytes = ByteString.copyFrom("test".getBytes());
		Image img = Image.newBuilder().setContent(imgBytes).build();

		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
			.addFeatures(Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION))
			.setImage(img)
			.build();

		LocalizedObjectAnnotation localizedObject = LocalizedObjectAnnotation.newBuilder()
			.setName("Top")
			.setBoundingPoly(BoundingPoly.newBuilder()
				.addNormalizedVertices(NormalizedVertex.newBuilder().setX(0.1f).setY(0.1f).build())
				.addNormalizedVertices(NormalizedVertex.newBuilder().setX(0.1f).setY(0.2f).build())
				.addNormalizedVertices(NormalizedVertex.newBuilder().setX(0.2f).setY(0.2f).build())
				.addNormalizedVertices(NormalizedVertex.newBuilder().setX(0.2f).setY(0.1f).build()))
			.build();

		BatchAnnotateImagesResponse batchResponse = BatchAnnotateImagesResponse.newBuilder()
			.addResponses(AnnotateImageResponse.newBuilder()
				.addLocalizedObjectAnnotations(localizedObject)
				.build())
			.build();

		when(mockClient.batchAnnotateImages(anyList())).thenReturn(batchResponse);

		// Mock behavior for extractColorProperties (use reflection to set the mock client)
		RGBColor mockColor = new RGBColor(255, 255, 255);
		GoogleImagenVisionService service = spy(new GoogleImagenVisionService());
		doReturn(mockColor).when(service).extractColorProperties(any(ByteString.class), eq(mockClient));

		// Prepare test image bytes
		byte[] imgBytesForTest = "test image bytes".getBytes();

		// Execute the method
		List<ClothAnalyzeData> result = service.analyzeImage(imgBytesForTest);

		// Verify results
		assertNotNull(result);
		assertFalse(result.isEmpty());

		ClothAnalyzeData clothAnalyzeData = result.get(0);
		assertEquals(ClothType.TOP, clothAnalyzeData.clothType());
		assertEquals("Top", clothAnalyzeData.clothName());
		assertEquals(new RGBColor(255, 255, 255), clothAnalyzeData.rgbColor());
		assertNotNull(clothAnalyzeData.leftTopVertex());
		assertNotNull(clothAnalyzeData.rightBottomVertex());
		assertNotNull(clothAnalyzeData.tristimulus());

		// Verify interactions
		verify(mockClient, times(1)).batchAnnotateImages(anyList());
	}
}
*/
