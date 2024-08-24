package org.example.image.redis.repository;

import org.example.image.redis.domain.entity.ColorEntity;
import org.springframework.data.repository.CrudRepository;

public interface ImageRedisRepository extends CrudRepository<ColorEntity, String> {

}
