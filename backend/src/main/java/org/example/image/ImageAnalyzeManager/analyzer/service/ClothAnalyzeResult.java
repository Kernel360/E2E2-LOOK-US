package org.example.image.ImageAnalyzeManager.analyzer.service;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClothAnalyzeResult {

	private final ClothType clothType;
	private final RGBColor rgbColor;

	private final NormalizedVertex2D leftTopVertex;
	private final NormalizedVertex2D rightBottomVertex;
}
