package org.example.image.repository;

import org.example.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {}
