package org.example.post.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;

	//TODO: 카테고리의 경우, 상의 -> 티셔츠 , 바지 -> ~팬츠 이런 식으로 상위 하위 카테고리 분류가 필요합니다.
	@Column(name = "category_content", nullable = false)
	private String categoryContent;

	public CategoryEntity(String categoryContent) {
		this.categoryContent = categoryContent;
	}
}
