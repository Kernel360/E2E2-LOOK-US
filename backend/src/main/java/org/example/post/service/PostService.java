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

	public ResponseEntity<PaginationResponseDto> getAllPostsOrderedBySortStrategy(PaginationRequestDto paginationRequestDto) {
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
