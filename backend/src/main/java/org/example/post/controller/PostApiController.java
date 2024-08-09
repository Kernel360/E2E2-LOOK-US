package org.example.post.controller;

import org.example.post.domain.dto.PostDto;
import org.example.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostApiController {

	private final PostService postService;

	@Operation(summary = "게시글 작성 API", description = "사용자가 게시글을 작성할 수 있다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@PostMapping(value = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<PostDto.CreatePostDtoResponse> createPost(
		@Valid @RequestPart("userRequest") PostDto.CreatePostDtoRequest userRequest,
		@RequestPart(value = "image") MultipartFile image) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		PostDto.CreatePostDtoResponse post = postService.createPost(userRequest, email, image);

		return ResponseEntity.status(HttpStatus.CREATED).body(post);
	}

	@GetMapping("/posts/{post_id}")
	public ResponseEntity<PostDto.PostDetailDtoResponse> getPostById(
		@PathVariable Long post_id
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(postService.getPostById(post_id));
	}

}
