package org.example.post.domain.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import org.example.post.domain.entity.HashtagEntity;
import org.springframework.web.multipart.MultipartFile;

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
	@JsonProperty("image_file")
	private MultipartFile imageFile;

	@JsonProperty("post_content")
	private String postContent;

	@JsonProperty("hashtag_content")
	private List<String> hashtagContents;

	public PostRequestDto(String userId, MultipartFile imageFile, String postContent, List<String> hashtagContents) {
		this.userId = userId;
		this.imageFile = imageFile;
		this.postContent = postContent;
		this.hashtagContents = hashtagContents;
	}

	// TODO: 리팩토링 필요
	public List<HashtagEntity> convertStringsToHashtags(List<String> hashtags) {
		return hashtags.stream()
			.map(HashtagEntity::new) // Convert each String to a new HashtagEntity
			.collect(Collectors.toList()); // Collect the results into a List<HashtagEntity>
	}
}
