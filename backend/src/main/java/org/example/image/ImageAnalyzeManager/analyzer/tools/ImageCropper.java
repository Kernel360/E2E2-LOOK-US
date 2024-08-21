package org.example.image.ImageAnalyzeManager.analyzer.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.cloud.vision.v1.NormalizedVertex;

public class ImageCropper {

	private final BufferedImage originalImage;

	public ImageCropper(byte[] imgBytes) {
		this.originalImage = createBufferdImageFromBytes(imgBytes);
	}

	public byte[] getCroppedImageByCornerPoint(
		NormalizedVertex leftTop,
		NormalizedVertex rightBottom
	) throws IOException {
		int height = originalImage.getHeight();
		int width = originalImage.getWidth();

		int upperLeftCornerX = (int)(width * leftTop.getX());
		int upperLeftCornerY = (int)(height * leftTop.getY());

		int bottomRightCornerX = (int)(width * rightBottom.getX());
		int bottomRightCornerY = (int)(height * rightBottom.getY());

		BufferedImage bufferedImage = this.originalImage.getSubimage(
			upperLeftCornerX,
			upperLeftCornerY,
			bottomRightCornerX - upperLeftCornerX,
			bottomRightCornerY - upperLeftCornerY
		);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpeg", baos);
		return baos.toByteArray();
	}

	private BufferedImage createBufferdImageFromBytes(byte[] imageData) {
		ByteArrayInputStream stream = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
