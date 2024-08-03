package org.example.post.controller;

import java.util.List;

import org.example.post.domain.dto.request.PostRequestDto;
import org.example.post.domain.dto.request.PaginationRequestDto;
import org.example.post.domain.dto.response.PaginationResponseDto;
import org.example.post.domain.dto.response.PostResponseDto;
import org.example.post.repository.PostRepository;
import org.example.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * The type Post api controller.
 */
@RestController
// @RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostApiController {

	private final PostRepository postRepository;
	private final PostService postService;
	//TODO: 로그인된 유저인지 확인하는 로직 필요, User가 아니여야 하는 거 아닌가...

	@PostMapping("/posts")
	public ResponseEntity<PostResponseDto> createPost(
		@Valid @RequestBody PostRequestDto postCreateRequestDto) {

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("name: " + name);
		PostResponseDto postResponseDto = postService.createPost(postCreateRequestDto);

		return ResponseEntity.ok(postResponseDto);
	}

	/**
	 * Gets all posts.
	 *
	 * @param searchHashtags 해시태그 검색어 목록
	 * @param searchString 게시글 내용 검색어
	 * @param sortField 정렬 기준 - 기본값 createdAt
	 * @param sortDirection 정렬 방식 - 기본값 DESC
	 * @param page 현재 페이지 - 기본값 0
	 * @param size 전체 페이지 사이즈 - 기본값 10
	 * @return 검색 결과에 대한 정렬, 페이지네이션을 포함한 게시물 리스트
	 */
	@GetMapping("/posts")		// TODO: searchHashtag 와 searchString 은 POST Method 바꿔서 @RequestBody로 받기
	public ResponseEntity<PaginationResponseDto> getAllPosts(
		@RequestParam(value = "searchHashtags", required = false) List<String> searchHashtags,
		@RequestParam(value = "searchString", required = false) String searchString,
		@RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
		@RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {

		PaginationRequestDto paginationRequestDto = new PaginationRequestDto(page, size, sortField, sortDirection,
			searchHashtags
			, searchString
		);

		return postService.getAllPostsOrderedBySortStrategy(paginationRequestDto);
	}
}
