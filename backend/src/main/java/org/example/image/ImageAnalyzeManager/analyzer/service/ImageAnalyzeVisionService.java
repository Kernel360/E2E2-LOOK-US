package org.example.image.ImageAnalyzeManager.analyzer.service;

import java.io.IOException;
import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;

public interface ImageAnalyzeVisionService {

	List<ClothAnalyzeData> analyzeImage(byte[] imageBytes) throws IOException;
}
