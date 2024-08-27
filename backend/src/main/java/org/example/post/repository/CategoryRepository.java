package org.example.post.repository;

import java.util.List;

import org.example.post.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
	List<CategoryEntity> findAllByCategoryContent(String categoryContent);

}
