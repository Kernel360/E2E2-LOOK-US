package org.example.post.repository.custom;

import lombok.Data;


@Data
public class CategoryAndColorSearchCondition {

	private Long categoryId;
	private int[] rgbColor;
}
