package org.example.post.controller;

import java.util.List;

import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.CategoryEntity;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/a1/posts")
@RequiredArgsConstructor
public class PostPublicController {
	private final PostService postService;

	@GetMapping("")
	public ResponseEntity<Page<PostDto.PostDtoResponse>> searchPost(
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) String postContent,
		@RequestParam(required = false) String hashtags,
		@RequestParam(required = false) int[] rgbColor,
		@RequestParam(required = false) String category
	) throws JsonProcessingException {

		// PostSearchCondition 객체를 생성하고 삼항 연산자를 이용해 간결하게 설정
		PostSearchCondition postSearchCondition = new PostSearchCondition();
		postSearchCondition.setPostContent(postContent != null ? postContent : null);
		postSearchCondition.setHashtags(hashtags != null ? hashtags : null);
		postSearchCondition.setRgbColor(rgbColor != null ? rgbColor : null);
		postSearchCondition.setCategory(category != null ? category : null);

		// RGB 색상에 따라 검색 로직을 분기 처리
		return ResponseEntity.status(HttpStatus.OK)
			.body(postSearchCondition.getRgbColor() == null
				? postService.findAllPosts(postSearchCondition, pageable)
				: postService.findAllPostsByRGB(postSearchCondition.getRgbColor(), pageable)
			);
	}

	// Permit All
	@Operation(summary = "게시글 상세 조회 API", description = "게시글 ID를 통해 게시글 조회에 필요한 데이터 반환")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})

	@GetMapping("/{post_id}")
	public ResponseEntity<PostDto.PostDetailDtoResponse> getPostById(
		@PathVariable Long post_id, HttpServletRequest request, HttpServletResponse response
	) throws JsonProcessingException {
		/* 조회수 로직 */
		postService.viewCount(post_id, request, response);

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
	public ResponseEntity<Integer> likeCount(@RequestBody PostDto.PostIdRequest likeRequest) {
		int likeCount = postService.likeCount(likeRequest.postId());
		return ResponseEntity.status(HttpStatus.OK).body(likeCount);
	}

	@Operation(summary = "카테고리별 게시글 가져오는 API", description = "카테고리 ID를 통해 해당하는 카테고리 게시글 반환")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@GetMapping("/category")
	public ResponseEntity<Page<PostDto.PostDtoResponse>> getPostsByCategory(
		@PathVariable Long categoryId,
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<PostDto.PostDtoResponse> posts = postService.findAllPostsByCategory(categoryId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(posts);

	}

	@Operation(summary = "카테고리 전체 가져오는 API", description = "카테고리 전체 반환")
	@GetMapping("/categoryAll")
	public ResponseEntity<List<CategoryEntity>> getCategoryAll() {
		List<CategoryEntity> categoryList = postService.getAllCategory();
		return ResponseEntity.status(HttpStatus.OK).body(categoryList);
	}

}

