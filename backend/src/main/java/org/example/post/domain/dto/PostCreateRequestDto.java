package org.example.post.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

	@NotBlank(message = "사용자 닉네임은 필수입니다.")
	@JsonProperty("user_id")
	private String userId;

	@NotBlank(message = "사진 업로드는 필수입니다.")
	@JsonProperty("image_src")
	private String imageSrc;

	@JsonProperty("post_content")
	private String postContent;

	public PostCreateRequestDto(String userId, String imageSrc, String postContent) {
		this.userId = userId;
		this.imageSrc = imageSrc;
		this.postContent = postContent;
	}
}
