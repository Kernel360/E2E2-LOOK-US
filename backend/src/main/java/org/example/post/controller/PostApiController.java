package org.example.post.controller;

import java.util.List;

import org.example.post.domain.dto.PostDto;
import org.example.post.domain.dto.request.PaginationRequestDto;
import org.example.post.domain.dto.response.PaginationResponseDto;
import org.example.post.domain.dto.response.PostResponseDto;
import org.example.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping(
		value = "/posts",
		consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
	)
	public ResponseEntity<PostDto.CreatePostDtoResponse> createPost(
		@Valid @ModelAttribute PostDto.CreatePostDtoRequest request
	) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		PostDto.CreatePostDtoResponse post = postService.createPost(request, name);

		return ResponseEntity.status(HttpStatus.CREATED).body(post);
	}

	@Operation(summary = "게시글 조회 API", description = "모든 게시글 조회 및 해시태그, 검색 키워드로 조회 가능하다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@GetMapping("/posts")
	public ResponseEntity<PaginationResponseDto> getAllPosts(
		@RequestParam(value = "searchHashtags", required = false) List<String> searchHashtags,
		@RequestParam(value = "searchString", required = false) String searchString,
		@RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
		@RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {

		PaginationRequestDto paginationRequestDto = new PaginationRequestDto(
			page, size, sortField, sortDirection,
			searchHashtags, searchString
		);

		return postService.getAllPostsOrderedBySortStrategy(paginationRequestDto);
	}

	@GetMapping("/posts/{post_id}")
	public ResponseEntity<PostResponseDto> getPostById(
		@PathVariable Long post_id
	) {
		return postService.getPostById(post_id);
	}

}
