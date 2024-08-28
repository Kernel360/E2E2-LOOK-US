package org.example.post.repository.custom;

import lombok.Data;


@Data
public class CategoryAndColorSearchCondition {

	private String category;
	private int[] rgbColor;
}
