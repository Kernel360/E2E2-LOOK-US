package org.example.image.resourceLocation.repository;

import org.example.image.resourceLocation.entity.ResourceLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceLocationRepository extends JpaRepository<ResourceLocationEntity, Long> {}
