package org.example.image.ImageAnalyzeManager;
import java.io.IOException;

import org.example.image.ImageAnalyzeManager.type.ImageAnalyzeData;

public interface ImageAnalyzeManager {

	// analyze image and store to DB
	void analyze(Long imageLocationId) throws IOException;

	// get analyzed data from DB
	ImageAnalyzeData getAnalyzedData(Long imageLocationId);

}
