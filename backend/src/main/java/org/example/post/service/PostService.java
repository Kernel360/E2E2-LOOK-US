package org.example.post.service;

import java.util.Optional;

import org.apache.catalina.security.SecurityUtil;
import org.example.post.domain.dto.PostCreateRequestDto;
import org.example.post.domain.dto.PostResponseDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.PostRepository;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;


	@Transactional
	public PostResponseDto createPost(PostCreateRequestDto postDto) {
		UserEntity user = userRepository.findByUsername(postDto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		PostEntity post = PostEntity.builder()
			.user(user)
			.imageUrl(postDto.getImageSrc())
			.postContent(postDto.getPostContent())
			.likeCount(0)
			.build();

		PostEntity savedPost = postRepository.save(post);
		return PostResponseDto.fromEntity(savedPost);
	}


}
