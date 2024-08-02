package org.example.post.controller;

import org.example.post.domain.dto.reqeust.PaginationRequestDto;
import org.example.post.domain.dto.response.PaginationResponseDto;
import org.example.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PostApiController {
	private final PostService postService;

	// TODO: 전체 게시글 조회, 비로그인 유저
	// TODO: 게시글 검색, PostStatus 확인 조건 필요
	@GetMapping("/posts")
	public ResponseEntity<PaginationResponseDto> getAllPosts(
		@RequestParam(value = "searchHashtag", required = false) String searchHashtag,
		@RequestParam(value = "searchString", required = false) String searchString,
		@RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
		@RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {

		PaginationRequestDto paginationRequestDto = new PaginationRequestDto(page, size, sortField, sortDirection,
			searchHashtag, searchString
		);

		return postService.getAllPostsOrderedBySortStrategy(paginationRequestDto);
	}

	// TODO: 게시글 내용 조회, 비로그인 유저

}
