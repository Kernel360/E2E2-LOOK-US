package org.example.post.controller;

import org.example.post.domain.dto.PaginationDto;
import org.example.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	@Operation(summary = "게시글 조회 API", description = "모든 게시글 조회 및 해시태그, 검색 키워드로 조회 가능하다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@GetMapping("/posts")
	public ResponseEntity<PaginationDto.PaginationDtoResponse> getAllPosts(
		@RequestParam(value = "searchHashtags", required = false) String searchHashtags,
		@RequestParam(value = "searchString", required = false) String searchString,
		@RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
		@RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {

		PaginationDto.PaginationDtoRequest paginationDto = new PaginationDto.PaginationDtoRequest(
			page, size, sortField, sortDirection, searchHashtags, searchString
		);

		// return postService.getAllPostsOrderedBySortStrategy(paginationDto);
		PaginationDto.PaginationDtoResponse allPostsOrderedBySortStrategy = postService.getAllPostsOrderedBySortStrategy(
			paginationDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(allPostsOrderedBySortStrategy);
	}

}
