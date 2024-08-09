package org.example.post.controller;

import org.example.post.domain.dto.PostDto;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostRepository postRepository;

	@Operation(summary = "게시글 조회 API", description = "모든 게시글 조회 및 해시태그, 검색 키워드로 조회 가능하다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "ok!!"),
		@ApiResponse(responseCode = "404", description = "Resource not found!!")
	})
	@GetMapping("/posts")
	public Page<PostDto.PostDtoResponse> searchPost(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) PostSearchCondition condition, Pageable pageable) {
		return postRepository.search(condition, pageable);
	}
}
