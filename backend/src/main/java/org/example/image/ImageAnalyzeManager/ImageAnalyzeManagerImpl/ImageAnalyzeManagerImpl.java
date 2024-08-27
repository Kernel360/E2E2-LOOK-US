package org.example.image.ImageAnalyzeManager.ImageAnalyzeManagerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.ImageAnalyzeManager.analyzer.entity.ClothAnalyzeDataEntity;
import org.example.image.ImageAnalyzeManager.analyzer.repository.ClothAnalyzeDataRepository;
import org.example.image.ImageAnalyzeManager.analyzer.service.ImageAnalyzeVisionService;
import org.example.image.ImageAnalyzeManager.analyzer.tools.ColorConverter;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import org.example.image.ImageAnalyzeManager.type.ImageAnalyzeData;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageAnalyzeManagerImpl implements ImageAnalyzeManager {

	private final ImageAnalyzeVisionService imageAnalyzeVisionService;
	private final ClothAnalyzeDataRepository clothAnalyzeDataRepository;

	private final ImageStorageManager imageStorageManager;

	@Override
	public void analyze(Long imageLocationId) throws IOException {
		StorageLoadResult result = imageStorageManager.loadImageByLocationId(imageLocationId);
		byte[] imageBytes = Files.readAllBytes(result.resource().getFile().toPath());

		imageAnalyzeVisionService.analyzeImage(imageBytes)
			.forEach(clothAnalyzeResult -> {
				clothAnalyzeDataRepository.save(
					ClothAnalyzeDataEntity
						.builder()
						.clothType(clothAnalyzeResult.clothType())
						.clothName(clothAnalyzeResult.clothName())
						.rgbColor(clothAnalyzeResult.rgbColor())
						.labColor(ColorConverter.RGBtoLAB(clothAnalyzeResult.rgbColor(), clothAnalyzeResult.tristimulus()))
						.boundingBox(
							List.of(
								clothAnalyzeResult.leftTopVertex(),
								clothAnalyzeResult.rightBottomVertex()
							)
						)
						.imageLocationId(imageLocationId)
						.build()
				);
			});
	}

	@Override
	public ImageAnalyzeData getAnalyzedData(Long imageLocationId) {

		// 1. gather pre-analyzed cloth data from db
		List<ClothAnalyzeData> clothAnalyzeDataList
			= this.clothAnalyzeDataRepository.findAllByImageLocationId(imageLocationId)
			.stream()
			.map(
				clothAnalyzeData -> ClothAnalyzeData.builder()
					.clothType(clothAnalyzeData.getClothType())
					.labColor(clothAnalyzeData.getLabColor())
					.rgbColor(clothAnalyzeData.getRgbColor())
					.leftTopVertex(clothAnalyzeData.getBoundingBox().get(0))
					.rightBottomVertex(clothAnalyzeData.getBoundingBox().get(1))
					.build()
			).toList();

		// 2. gather other data from db ...
		// TODO: add more gathering logic if needed

		// 3. return as single DTO
		return new ImageAnalyzeData(
			clothAnalyzeDataList
		);
	}
}
