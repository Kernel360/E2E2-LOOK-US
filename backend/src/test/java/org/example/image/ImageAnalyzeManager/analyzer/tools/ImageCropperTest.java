package org.example.image.ImageAnalyzeManager.analyzer.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.google.cloud.vision.v1.NormalizedVertex;

class ImageCropperTest {
	// Cropping an image with valid corner points
	@Test
	public void test_cropping_image_with_valid_corner_points() throws IOException {
		byte[] imgBytes = Files.readAllBytes(Paths.get("src/test/resources/test_image.jpg"));
		ImageCropper cropper = new ImageCropper(imgBytes);

		NormalizedVertex leftTop = NormalizedVertex.newBuilder().setX(0.1f).setY(0.1f).build();
		NormalizedVertex rightBottom = NormalizedVertex.newBuilder().setX(0.5f).setY(0.5f).build();

		byte[] croppedImageBytes = cropper.getCroppedImageByCornerPoint(leftTop, rightBottom);

		assertNotNull(croppedImageBytes);
		BufferedImage croppedImage = ImageIO.read(new ByteArrayInputStream(croppedImageBytes));
		assertEquals(0.4 * cropper.originalImage.getWidth(), croppedImage.getWidth(), 1);
		assertEquals(0.4 * cropper.originalImage.getHeight(), croppedImage.getHeight(), 1);
	}

}