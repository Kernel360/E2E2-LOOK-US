package org.example.post.repository.custom;

import java.util.List;

import lombok.Data;

@Data
public class PostSearchCondition {
	private String postContent;
	private String hashtags;
}
