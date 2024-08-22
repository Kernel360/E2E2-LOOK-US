package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService;

import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.type.GoogleImagenVisionDto;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.clothTypeMapper.ClothTypeMapper;
import org.example.image.ImageAnalyzeManager.analyzer.service.ClothAnalyzeService;
import org.example.image.ImageAnalyzeManager.analyzer.tools.ImageCropper;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.NormalizedVertex;
import com.google.protobuf.ByteString;
import com.google.type.Color;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleImagenVisionService implements ClothAnalyzeService {

	// ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

	@Override
	public List<ClothAnalyzeData> analyzeImage(byte[] imgBytes) throws IOException {

		// Initialize client that will be used to send requests. This client only needs to be created
		// once, and can be reused for multiple requests. After completing all of your requests, call
		// the "close" method on the client to safely clean up any remaining background resources.

		// Ref: https://googleapis.dev/nodejs/vision/latest/google.api.ClientLibrarySettings.html#.create

		// TODO: 추후에 환경 변수로 변경해야 합니다.
		Credentials credentials = GoogleCredentials.fromStream(
			new FileInputStream("/home/kyeu/goinfree/kernel360/BackendApp/backend/kyeu_api_key.json")
		);

		ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
																.setCredentialsProvider(
																	FixedCredentialsProvider.create(credentials)
																).build();

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {

			// 0. prepare analyze process
			ByteString imageByteString = ByteString.copyFrom(imgBytes);
			ImageCropper imageCropper = new ImageCropper(imgBytes);

			// 1. detect clothes
			List<GoogleImagenVisionDto.ClothDetection> detectedClothList = detectClothObjects(imageByteString, client);

			// 2. return cloth data including its dominant rgb color
			return detectedClothList.stream()
									.map(cloth -> {
										try {
											NormalizedVertex leftTopVertex = cloth.normalizedVertices().get(0);
											NormalizedVertex rightBottomVertex = cloth.normalizedVertices().get(2);

											// crop image to fit each cloth
											byte[] croppedImage = imageCropper.getCroppedImageByCornerPoint(
												leftTopVertex, rightBottomVertex
											);

											// send analyze request to google api
											RGBColor rgbColor = extractColorProperties(
												ByteString.copyFrom(croppedImage), client
											);

											// create analyze result DTO
											return ClothAnalyzeData
												.builder()
												.clothType(cloth.clothType())
												.rgbColor(rgbColor)
												.leftTopVertex(
													new NormalizedVertex2D(leftTopVertex.getX(), leftTopVertex.getY())
												)
												.rightBottomVertex(
													new NormalizedVertex2D(rightBottomVertex.getX(), rightBottomVertex.getY())
												)
												.build();

										} catch (IOException e) {
											throw new RuntimeException(e);
										}
									}).toList();
		}
	}

	/**
	 * Detects localized objects in the specified local image.
	 *
	 * @throws Exception on errors while closing the client.
	 * @throws IOException on Input/Output errors.
	 */
	private List<GoogleImagenVisionDto.ClothDetection> detectClothObjects(
		ByteString imgBytes,
		ImageAnnotatorClient client
	) {
		List<AnnotateImageRequest> requests = new ArrayList<>();

		Image img = Image.newBuilder().setContent(imgBytes).build();
		AnnotateImageRequest request =
			AnnotateImageRequest.newBuilder()
								.addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
								.setImage(img)
								.build();
		requests.add(request);

		// Perform the request
		BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);

		List<AnnotateImageResponse> responses = response.getResponsesList();
		List<LocalizedObjectAnnotation> entities = responses.get(0).getLocalizedObjectAnnotationsList();

		// Detects image properties such as color frequency from the specified local image.
		Map<String, GoogleImagenVisionDto.ClothDetection> detections = new HashMap<>();

		for (LocalizedObjectAnnotation entity : entities) {

			String rawObjectName = entity.getName();
			ClothTypeMapper.toCategory(rawObjectName)
						   .ifPresent(clothType -> {
								   detections.put(
									   rawObjectName,
									   new GoogleImagenVisionDto.ClothDetection(
										   clothType,
										   entity.getBoundingPoly().getNormalizedVerticesList()
									   )
								   );
							   });
		}

		return new ArrayList<>(detections.values());
	}

	private static RGBColor extractColorProperties(
		ByteString imgBytes,
		ImageAnnotatorClient client
	) {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		Image img = Image.newBuilder().setContent(imgBytes).build();

		// NOTE: get only 1 result.
		// TODO: 해당 값이 가장 높은 점수 색상 인지 검증 필요하다.
		Feature feat = Feature.newBuilder()
							  .setType(Feature.Type.IMAGE_PROPERTIES)
							  .setMaxResults(1)
							  .build();

		AnnotateImageRequest request =
			AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
		AnnotateImageResponse res = response.getResponsesList().get(0);

		if (res.hasError()) {
			log.error("Google Imagen API Error: {}", res.getError().getMessage());
			throw new RuntimeException(res.getError().getMessage());
		}

		DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
		Color color = colors.getColorsList().get(0).getColor();

		return new RGBColor((int)color.getRed(), (int)color.getGreen(), (int)color.getBlue());
	}
}
