package org.example.post.domain.dto.request;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

	@NotBlank(message = "사용자 닉네임은 필수입니다.")
	@JsonProperty("user_id")
	private String userId;

	@NotBlank(message = "사진 업로드는 필수입니다.")
	@JsonProperty("image_src")
	private String imageSrc;

	@JsonProperty("post_content")
	private String postContent;

	private List<String> hashtagContents;

	public PostRequestDto(String userId, String imageSrc, String postContent, List<String> hashtagContents) {
		this.userId = userId;
		this.imageSrc = imageSrc;
		this.postContent = postContent;
		this.hashtagContents = hashtagContents;
	}

	public List<String> getHashtagList() {
		return	hashtagContents;
	}
}
