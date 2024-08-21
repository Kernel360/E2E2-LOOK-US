package org.example.image.ImageAnalyzeManager.ImageAnalyzeManagerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.example.image.ImageAnalyzeManager.type.AnalyzeData;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.ImageAnalyzeManager.analyzer.entity.ClothAnalyzeDataEntity;
import org.example.image.ImageAnalyzeManager.analyzer.repository.ClothAnalyzeDataRepository;
import org.example.image.ImageAnalyzeManager.analyzer.service.ClothAnalyzeService;
import org.example.image.ImageAnalyzeManager.analyzer.tools.ColorConverter;
import org.example.image.imageStorageManager.type.StorageFindResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageAnalyzeManagerImpl implements ImageAnalyzeManager {

	private final ClothAnalyzeService clothAnalyzeService;
	private final ClothAnalyzeDataRepository clothAnalyzeDataRepository;

	private final ImageStorageManager imageStorageManager;

	@Override
	public void requestAnalyze(Long resourceLocationId) throws IOException {
		StorageFindResult result = imageStorageManager.findResourceById(resourceLocationId);
		byte[] imageBytes = Files.readAllBytes(result.resource().getFile().toPath());

		clothAnalyzeService.analyzeImage(imageBytes)
						   .forEach(clothAnalyzeResult -> {
							   clothAnalyzeDataRepository.save(
								   ClothAnalyzeDataEntity
									   .builder()
									   .clothType(clothAnalyzeResult.getClothType())
									   .labColor(ColorConverter.RGBtoLAB(clothAnalyzeResult.getRgbColor()))
									   .boundingBox(
										   List.of(
											   clothAnalyzeResult.getLeftTopVertex(),
											   clothAnalyzeResult.getRightBottomVertex()
										   )
									   )
									   .resourceLocationId(resourceLocationId)
									   .build()
							   );
						   });
	}

	@Override
	public AnalyzeData getAnaylzedData(Long resourceLocationId) {
		AnalyzeData analyzeData = new AnalyzeData();

		// 1. gather cloth data ...
		analyzeData.clothAnalyzeDataList
			= this.clothAnalyzeDataRepository.findAllByResourceLocationId(resourceLocationId)
											 .stream()
											 .map(
												 clothAnalyzeData -> AnalyzeData.ClothAnalyzeData.builder()
													 .clothType(clothAnalyzeData.getClothType())
													 .labColor(clothAnalyzeData.getLabColor())
													 .LeftTop(clothAnalyzeData.getBoundingBox().get(0))
													 .RightBottom(clothAnalyzeData.getBoundingBox().get(1))
													 .build()
											 ).toList();
		// 2. gather other data ...

		return analyzeData;
	}
}
