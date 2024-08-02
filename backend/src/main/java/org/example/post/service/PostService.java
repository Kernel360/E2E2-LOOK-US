package org.example.post.service;

import java.util.stream.Collectors;

import org.example.post.domain.dto.reqeust.PostCreateRequestDto;
import org.example.post.domain.dto.response.PostResponseDto;
import org.example.post.domain.dto.reqeust.PaginationRequestDto;
import org.example.post.domain.dto.response.PaginationResponseDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.PostRepository;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

		PostEntity postEntity = new PostEntity(
			user,
			postDto.getPostContent(),
			postDto.getImageSrc(),
			0, // Initialize likeCount
			PostStatus.PUBLISHED // Set default status
		);

		PostEntity savedPost = postRepository.save(postEntity);
		PostResponseDto postResponseDto = new PostResponseDto();
		postResponseDto.setPostId(savedPost.getPostId());
		return postResponseDto;
	}

	public ResponseEntity<PaginationResponseDto> getAllPostsOrderedBySortStrategy(
		PaginationRequestDto paginationRequestDto) {
		int page = paginationRequestDto.getPage();
		int size = paginationRequestDto.getSize();
		String hashtag = paginationRequestDto.getSearchHashtag();
		String searchString = paginationRequestDto.getSearchString();


		String direction = paginationRequestDto.getSortDirection();
		String field = paginationRequestDto.getSortField();
		Sort sort = Sort.by(field).descending();
		if (direction.equalsIgnoreCase("ASC")) {
			sort = Sort.by(field).ascending();
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<PostEntity> postPage;
		if (!searchString.isBlank()) {
			postPage = postRepository.findAllByPostContentContainingAndPostStatus(searchString, PostStatus.PUBLISHED, pageable);
		} else {
			postPage = postRepository.findAllByPostStatus(PostStatus.PUBLISHED, pageable);
		}

		if (postPage == null) {
			throw new NullPointerException("No posts found");
		}

		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setPage(page);
		paginationResponseDto.setSize(size);
		paginationResponseDto.setTotalElements(postPage.getTotalElements());
		paginationResponseDto.setTotalPages(postPage.getTotalPages());
		paginationResponseDto.setPostResponseDtoList(postPage.stream()
			.map(postEntity -> {
				PostResponseDto postResponseDto = new PostResponseDto();
				postResponseDto.setPostId(postEntity.getPostId());
				postResponseDto.setPostContent(postEntity.getPostContent());
				postResponseDto.setLikeCount(postEntity.getLikeCount());
				return postResponseDto;
			}).collect(Collectors.toList()));

		return ResponseEntity.status(HttpStatus.OK)
			.body(paginationResponseDto);
	}

}
