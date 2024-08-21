package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;

import com.google.cloud.vision.v1.NormalizedVertex;

public class GoogleImagenVisionDto {

	public record ClothDetection(
		ClothType clothType,
		List<NormalizedVertex> normalizedVertices
	) {}

	public record ColorDetection(
		RGBColor rgbColor
	) {}


}
