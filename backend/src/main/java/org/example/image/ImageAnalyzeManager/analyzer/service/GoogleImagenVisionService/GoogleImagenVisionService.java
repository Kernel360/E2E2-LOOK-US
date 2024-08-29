package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.type.GoogleImagenVisionDto;
import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils.ClothTypeMapper;
import org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils.GoogleApiCredentialRoller;
import org.example.image.ImageAnalyzeManager.analyzer.service.ImageAnalyzeVisionService;
import org.example.image.ImageAnalyzeManager.analyzer.tools.ImageCropper;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.example.config.log.LogExecution;
import org.springframework.stereotype.Service;

import com.google.api.gax.core.FixedCredentialsProvider;
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
public class GoogleImagenVisionService implements ImageAnalyzeVisionService {

	private final GoogleApiCredentialRoller apiKeyRoller
		= GoogleApiCredentialRoller.fromEnv("GOOGLE_VISION_API_KEYS");

	@Override
	@LogExecution
	public List<ClothAnalyzeData> analyzeImage(byte[] imgBytes) throws IOException {

		// Initialize client that will be used to send requests. This client only needs to be created
		// once, and can be reused for multiple requests. After completing all of your requests, call
		// the "close" method on the client to safely clean up any remaining background resources.

		// Ref: https://googleapis.dev/nodejs/vision/latest/google.api.ClientLibrarySettings.html#.create


		ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
			.setCredentialsProvider(
				FixedCredentialsProvider.create( apiKeyRoller.getCredential() )
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

						// Image Pre-processing
						double brightness = calculateAverageBrightness(byteArrayToBufferedImage(croppedImage));
						double temperature = estimateColorTemperature(byteArrayToBufferedImage(croppedImage));
						float[] tristimulus = calculateTristimulus(brightness, temperature);

						// Adjust the cropped image brightness for D65
						byte[] adjustedImageBytes = adjustImageBrightnessForD65(croppedImage, tristimulus);

						// send analyze request to google api
						RGBColor rgbColor = extractColorProperties(
							ByteString.copyFrom(adjustedImageBytes), client
						);
						RGBColor rgbColor1 = extractColorProperties(
							ByteString.copyFrom(croppedImage), client
						);

						// create analyze result DTO
						return ClothAnalyzeData
							.builder()
							.clothType(cloth.clothType())
							.clothName(cloth.clothName())
							.rgbColor(rgbColor)
							.leftTopVertex(
								new NormalizedVertex2D(leftTopVertex.getX(), leftTopVertex.getY())
							)
							.rightBottomVertex(
								new NormalizedVertex2D(rightBottomVertex.getX(), rightBottomVertex.getY())
							)
							.tristimulus(tristimulus)
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
	 * @throws Exception   on errors while closing the client.
	 * @throws IOException on Input/Output errors.
	 */
	@LogExecution
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
			Optional<ClothType> clothTypeOptional = ClothTypeMapper.toCategory(rawObjectName);

			if (clothTypeOptional.isPresent()) {
				ClothType clothType = clothTypeOptional.get();
				detections.put(
					rawObjectName,
					new GoogleImagenVisionDto.ClothDetection(
						clothType,
						rawObjectName,
						entity.getBoundingPoly().getNormalizedVerticesList()
					)
				);
			} else if(!rawObjectName.equals("Person")){
				log.warn("Cloth type not found for object name: {}", rawObjectName);
			}
		}

		return new ArrayList<>(detections.values());
	}

	@LogExecution
	protected static RGBColor extractColorProperties(
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

	// byte[]를 BufferedImage로 변환
	private static BufferedImage byteArrayToBufferedImage(byte[] imageData) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		return ImageIO.read(bais);
	}

	// 평균 밝기 계산
	private static double calculateAverageBrightness(BufferedImage image) {
		long sumBrightness = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				java.awt.Color color = new java.awt.Color(image.getRGB(x, y));

				// Coefficient : Weights of RGB based on Rec. 709 Standard
				int brightness = (int)(0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue());
				sumBrightness += brightness;
			}
		}
		return (double)sumBrightness / (width * height);
	}

	// 색 온도 추정
	private static double estimateColorTemperature(BufferedImage image) {
		long sumRed = 0, sumGreen = 0, sumBlue = 0;
		int width = image.getWidth();
		int height = image.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
				sumRed += color.getRed();
				sumGreen += color.getGreen();
				sumBlue += color.getBlue();
			}
		}

		double avgRed = (double)sumRed / (width * height);
		double avgGreen = (double)sumGreen / (width * height);
		double avgBlue = (double)sumBlue / (width * height);

		// 색 온도를 계산하기 위해 CIE 1931 Color Space를 활용한 간단한 추정
		return 2.0 * avgRed - avgGreen - avgBlue;
	}

	// Tristimulus 값 계산
	private static float[] calculateTristimulus(double brightness, double colorTemperature) {
		// TODO: 수정 필요 할수도
		// 여기서는 간단히 XYZ로 변환하기 위한 임의의 계산식을 사용
		double X = brightness * 0.5;
		double Y = brightness;
		double Z = brightness / colorTemperature;

		return new float[] {(float)X, (float)Y, (float)Z};
	}

	@LogExecution
	private static byte[] adjustImageBrightnessForD65(byte[] imageBytes, float[] tristimulus) throws IOException {
		// Convert byte array to BufferedImage
		BufferedImage image = byteArrayToBufferedImage(imageBytes);

		// Adjust image brightness based on tristimulus
		BufferedImage adjustedImage = adjustImageBrightnessForD65(image, tristimulus);

		// Convert BufferedImage back to byte array
		return bufferedImageToByteArray(adjustedImage);
	}

	private static BufferedImage adjustImageBrightnessForD65(BufferedImage image, float[] tristimulus) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage adjustedImage = new BufferedImage(width, height, image.getType());

		// D65 reference values
		double X_d65 = 95.047;
		double Y_d65 = 100.000;
		double Z_d65 = 108.883;

		// Extract tristimulus values
		double X = tristimulus[0];
		double Y = tristimulus[1];
		double Z = tristimulus[2];

		// Calculate scale factor to adjust the brightness
		double scaleFactor = Y_d65 / Y;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				java.awt.Color color = new java.awt.Color(image.getRGB(x, y));

				// Extract RGB values
				double r = color.getRed() / 255.0;
				double g = color.getGreen() / 255.0;
				double b = color.getBlue() / 255.0;

				// Convert RGB to XYZ
				double X_rgb = r * 0.4124 + g * 0.3576 + b * 0.1805;
				double Y_rgb = r * 0.2126 + g * 0.7152 + b * 0.0722;
				double Z_rgb = r * 0.0193 + g * 0.1192 + b * 0.9505;

				// Adjust XYZ values based on D65
				X_rgb *= scaleFactor;
				Y_rgb *= scaleFactor;
				Z_rgb *= scaleFactor;

				// Convert adjusted XYZ back to RGB
				int red = (int) Math.min(255, (X_rgb * 3.2406 - Y_rgb * 1.5372 - Z_rgb * 0.4986) * 255);
				int green = (int) Math.min(255, (-X_rgb * 0.9689 + Y_rgb * 1.8758 + Z_rgb * 0.0415) * 255);
				int blue = (int) Math.min(255, (X_rgb * 0.0557 - Y_rgb * 0.2040 + Z_rgb * 1.0570) * 255);

				// Ensure values are within valid RGB range
				red = Math.max(0, Math.min(255, red));
				green = Math.max(0, Math.min(255, green));
				blue = Math.max(0, Math.min(255, blue));

				// Set the new RGB value
				adjustedImage.setRGB(x, y, new java.awt.Color(red, green, blue).getRGB());
			}
		}

		return adjustedImage;
	}

	private static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		return baos.toByteArray();
	}
}
