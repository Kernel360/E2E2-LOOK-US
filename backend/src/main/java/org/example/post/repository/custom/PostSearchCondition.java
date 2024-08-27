package org.example.post.repository.custom;


import lombok.Data;

@Data
public class PostSearchCondition {
	private String postContent;
	private String hashtags;
	private int[] rgbColor;
}
