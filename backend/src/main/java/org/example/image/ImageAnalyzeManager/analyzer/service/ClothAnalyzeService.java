package org.example.image.ImageAnalyzeManager.analyzer.service;

import java.io.IOException;
import java.util.List;

public interface ClothAnalyzeService {

	List<ClothAnalyzeResult> analyzeImage(byte[] imageBytes) throws IOException;
}
