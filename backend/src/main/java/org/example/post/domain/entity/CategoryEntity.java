package org.example.post.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "category")
	private String category;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private CategoryEntity parent;

	@OneToMany(mappedBy = "parent")
	private List<CategoryEntity> children = new ArrayList<>();


	@Builder
	public CategoryEntity(String category, PostEntity post, CategoryEntity parent) {
		this.category = category;
		this.parent = parent;
	}
}

