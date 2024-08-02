// package org.example.post.domain.dto;
//
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
//
// import org.example.post.domain.entity.PostEntity;
// import org.example.post.domain.entity.UserPostLikesEntity;
// import org.example.post.domain.enums.PostStatus;
// import org.example.user.domain.entity.member.UserEntity;
//
// import com.fasterxml.jackson.annotation.JsonProperty;
//
// import lombok.Builder;
// import lombok.Getter;
// import lombok.Setter;
//
// @Getter
// @Setter
// public class PostResponseDto {
//
//
// 	@JsonProperty("post_id")
// 	private final String postId;
//
//
//
// 	@Builder
// 	public PostResponseDto(String postId) {
// 		this.postId = postId;
// 	}
//
// 	public static PostResponseDto fromEntity(PostEntity post) {
// 		return PostResponseDto.builder()
// 			.postId(post.getPostId().toString())
// 			.build();
// 	}
// }
