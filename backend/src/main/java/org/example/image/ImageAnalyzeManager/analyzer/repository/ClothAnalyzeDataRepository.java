package org.example.image.ImageAnalyzeManager.analyzer.repository;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.entity.ClothAnalyzeDataEntity;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothAnalyzeDataRepository extends JpaRepository<ClothAnalyzeDataEntity, Integer> {

	List<ClothAnalyzeDataEntity> findAllByImageLocationId(Long imageLocationId);
  List<ClothAnalyzeDataEntity> findAllByRgbColor(RGBColor rgbColor);

}
