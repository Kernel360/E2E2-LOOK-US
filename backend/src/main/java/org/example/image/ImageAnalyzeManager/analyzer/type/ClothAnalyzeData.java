package org.example.image.ImageAnalyzeManager.analyzer.type;

import lombok.Builder;

@Builder
public record ClothAnalyzeData(
	ClothType clothType,
	String clothName,
	LabColor labColor,
	RGBColor rgbColor,
	NormalizedVertex2D leftTopVertex,
	NormalizedVertex2D rightBottomVertex,
	float[] tri
) {}
