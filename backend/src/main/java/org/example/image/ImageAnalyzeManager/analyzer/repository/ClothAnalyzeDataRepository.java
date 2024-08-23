package org.example.image.ImageAnalyzeManager.analyzer.repository;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.entity.ClothAnalyzeDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothAnalyzeDataRepository extends JpaRepository<ClothAnalyzeDataEntity, Integer> {

	List<ClothAnalyzeDataEntity> findAllByResourceLocationId(Long ResourceLocationId);
}
