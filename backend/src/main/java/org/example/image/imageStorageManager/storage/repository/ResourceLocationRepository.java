package org.example.image.imageStorageManager.storage.repository;

import org.example.image.imageStorageManager.storage.entity.ResourceLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceLocationRepository extends JpaRepository<ResourceLocationEntity, Long> {}
