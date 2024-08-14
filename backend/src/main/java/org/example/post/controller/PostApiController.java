package org.example.post.controller;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.post.domain.dto.PostDto;
import org.example.post.service.PostService;
import org.example.user.domain.entity.member.UserEntity;
import org.example.util.ClassUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostApiController {

	private final PostService postService;

	// Permit Only User
	@Operation(summary = "게시글 작성 API", description = "사용자가 게시글을 작성할 수 있다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<PostDto.CreatePostDtoResponse> createPost(
		@Valid @RequestPart("userRequest") PostDto.CreatePostDtoRequest userRequest,
		@RequestPart(value = "image") MultipartFile image) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		PostDto.CreatePostDtoResponse post = postService.createPost(userRequest, email, image);

		return ResponseEntity.status(HttpStatus.CREATED).body(post);
	}

	//TODO: request body 로 날리기
	@Operation(summary = "게시글 좋아요 API", description = "사용자가 게시글에 좋아요를 누를 수 있다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@PatchMapping("/likes")
	public ResponseEntity<Boolean> like(@RequestBody PostDto.PostIdRequest likeRequest,
		Authentication authentication) {
		Boolean like = postService.like(likeRequest.postId(), authentication.getName());
		// String message = like ? "좋아요 완료" : "좋아요 취소";
		return ResponseEntity.status(HttpStatus.OK).body(like);
	}

	@DeleteMapping("")
	public ResponseEntity<Void> deletePost(@RequestBody PostDto.PostIdRequest deleteRequest, Authentication authentication) {

		postService.delete(deleteRequest.postId(), authentication.getName());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
