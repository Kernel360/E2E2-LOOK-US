package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.type;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;

import com.google.cloud.vision.v1.NormalizedVertex;

public class GoogleImagenVisionDto {

	public record ClothDetection(
		ClothType clothType,
		String clothName,
		List<NormalizedVertex> normalizedVertices
	) {}
}
