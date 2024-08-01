package org.example.post.controller;

import org.example.post.domain.dto.PostCreateRequestDto;
import org.example.post.domain.dto.PostResponseDto;
import org.example.post.repository.PostRepository;
import org.example.post.service.PostService;
import org.example.user.domain.entity.member.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
// @RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostApiController {

	private final PostRepository postRepository;
	private final PostService postService;
	//TODO: 로그인된 유저인지 확인하는 로직 필요, User가 아니여야 하는 거 아닌가...

	@PostMapping("/posts")
	public ResponseEntity<PostResponseDto> createPost(
		@Valid @RequestBody PostCreateRequestDto postCreateRequestDto) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("name: " + name);
		PostResponseDto postResponseDto = postService.createPost(postCreateRequestDto);
		return ResponseEntity.ok(postResponseDto);
	}

}
