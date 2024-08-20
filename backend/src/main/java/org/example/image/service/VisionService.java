package org.example.image.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.protobuf.ByteString;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VisionService {

	/**
	 * Detects localized objects in the specified local image.
	 *
	 * @param filePath The path to the file to perform localized object detection on.
	 * @throws Exception on errors while closing the client.
	 * @throws IOException on Input/Output errors.
	 */
	public void detectLocalizedObjects(String filePath) throws IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		AnnotateImageRequest request =
			AnnotateImageRequest.newBuilder()
				.addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
				.setImage(img)
				.build();
		requests.add(request);

		// Initialize client that will be used to send requests. This client only needs to be created
		// once, and can be reused for multiple requests. After completing all of your requests, call
		// the "close" method on the client to safely clean up any remaining background resources.
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			// Perform the request
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			// TODO: 객체 생성해서 responses 에서 받아서 리턴 해야함
			// TODO: ClothType 수정 및 카테고리화 작업 필요

			// Display the results
			for (AnnotateImageResponse res : responses) {
				for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
					System.out.format("Object name: %s%n", entity.getName());	// Object name -> ClothType
					System.out.format("Confidence: %s%n", entity.getScore());	// Confidence
					System.out.format("Normalized Vertices:%n");				// Normalized Vertices list(x,y)
					entity
						.getBoundingPoly()
						.getNormalizedVerticesList()
						.forEach(vertex -> System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY()));
				}
			}
		}
	}

}