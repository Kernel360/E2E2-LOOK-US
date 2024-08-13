package org.example.post.controller;

import org.example.post.domain.dto.PostDto;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/a1")
@RequiredArgsConstructor
public class PostPublicController {
	private final PostService postService;

	// Permit All
	@Operation(summary = "게시글 조회 API", description = "모든 게시글 조회 및 해시태그, 검색 키워드로 조회 가능하다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@GetMapping("/posts")
	public ResponseEntity<Page<PostDto.PostDtoResponse>> searchPost(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) PostSearchCondition postSearchCondition,
		Pageable pageable
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(postService.findAllPosts(postSearchCondition, pageable));
	}

	// Permit All
	@GetMapping("/posts/{post_id}")
	public ResponseEntity<PostDto.PostDetailDtoResponse> getPostById(
		@PathVariable Long post_id
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(postService.getPostById(post_id));
	}

	//TODO : Request Body로 날리기
	@Operation(summary = "게시글 좋아요 수 가져오는 API", description = "게시글의 좋아요 수를 가져온다")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@GetMapping("/likes")
	public ResponseEntity<Integer> likeCount(@RequestBody PostDto.PostLikeRequest likeRequest) {
		int likeCount = postService.likeCount(likeRequest.postId());
		return ResponseEntity.status(HttpStatus.OK).body(likeCount);
	}
}
