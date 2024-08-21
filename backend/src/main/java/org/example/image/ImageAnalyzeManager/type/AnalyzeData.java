package org.example.image.ImageAnalyzeManager.type;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;

import lombok.Builder;

public class AnalyzeData {

	public List<ClothAnalyzeData> clothAnalyzeDataList;

	@Builder
	public record ClothAnalyzeData(
		ClothType clothType,
		LabColor labColor,
		NormalizedVertex2D LeftTop,
		NormalizedVertex2D RightBottom
	) {}
}


