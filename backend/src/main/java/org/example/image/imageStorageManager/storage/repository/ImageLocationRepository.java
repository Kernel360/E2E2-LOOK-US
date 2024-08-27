package org.example.image.imageStorageManager.storage.repository;

import org.example.image.imageStorageManager.storage.entity.ImageLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageLocationRepository extends JpaRepository<ImageLocationEntity, Long> {}
