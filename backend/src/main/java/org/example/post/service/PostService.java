package org.example.post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.post.common.PostMapper;
import org.example.post.domain.dto.request.PaginationRequestDto;
import org.example.post.domain.dto.request.PostRequestDto;
import org.example.post.domain.dto.response.PaginationResponseDto;
import org.example.post.domain.dto.response.PostResponseDto;
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
	public PostDto.CreatePostDtoResponse createPost(PostDto.CreatePostDtoRequest postDto,
		String name, MultipartFile image) {
		UserEntity user = userRepository.findByUsername(name)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		StorageSaveResult storageSaveResult = imageStorageManager.saveResource(image,
			StorageType.LOCAL_FILE_SYSTEM);

		PostEntity postEntity = new PostEntity(
			user,
			postDto.postContent(),
			storageSaveResult.resourceLocationId(),
			0, // Initialize likeCount
			PostStatus.PUBLISHED, // Set default status
			postDto.convertStringsToHashtags(postDto.getHashtagContents())
		);

		PostEntity savedPost = postRepository.save(postEntity);
		return PostDto.CreatePostDtoResponse.toDto(savedPost);
	}

	public ResponseEntity<PaginationResponseDto> getAllPostsOrderedBySortStrategy(
		PaginationRequestDto paginationRequestDto) {

		// get information for pagination by DTO
		int page = paginationRequestDto.getPage();
		int size = paginationRequestDto.getSize();
		List<String> hashtagList = paginationRequestDto.getSearchHashtagList();
		String searchString = paginationRequestDto.getSearchString();
		String direction = paginationRequestDto.getSortDirection();
		String field = paginationRequestDto.getSortField();

		// sort by field ordered by descending
		Sort sort = Sort.by(field).descending();
		if (direction.equalsIgnoreCase("ASC")) {
			// sort by field ordered by ascending
			sort = Sort.by(field).ascending();
		}

		// pagination
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<PostEntity> postPage;

		// search all posts
		// searchString null and hashtagList null
		postPage = postRepository.findAllByPostStatus(PostStatus.PUBLISHED, pageable);

		// search posts containing searchString(post content)
		// searchString not null
		if (searchString != null && !searchString.isBlank()) {
			postPage = postRepository.findAllByPostContentContainingAndPostStatus(searchString, PostStatus.PUBLISHED,
				pageable);
		}

		// search posts containing hashtag content
		// hashtagList not null
		if (hashtagList != null && !hashtagList.isEmpty()) {
			postPage = postRepository.findAllByHashtagsContainingAndPostStatus(hashtagList, PostStatus.PUBLISHED,
				pageable);
		}

		// search posts containing both searchString and hashtag content
		// get only some posts which must have both elements
		if (hashtagList != null && searchString != null && !hashtagList.isEmpty() && !searchString.isBlank()) {
			postPage = postRepository.findAllByPostContentContainingAndHashtagsContainingAndPostStatus(searchString,
				hashtagList, PostStatus.PUBLISHED, pageable);
		}

		// find no post about search condition
		// TODO: 수정 예정
		if (postPage == null) {
			throw new NullPointerException("No posts found");
		}

		// DTO for return data
		PaginationResponseDto paginationResponseDto = new PaginationResponseDto();
		paginationResponseDto.setPage(page);
		paginationResponseDto.setSize(size);
		paginationResponseDto.setTotalElements(postPage.getTotalElements());
		paginationResponseDto.setTotalPages(postPage.getTotalPages());

		paginationResponseDto.setPostResponseDtoList(postPage.stream()
															 .map(postEntity -> {
																 PostResponseDto postResponseDto = new PostResponseDto();
																 postResponseDto.setPostId(postEntity.getPostId());
																 postResponseDto.setPostContent(
																	 postEntity.getPostContent());
																 postResponseDto.setLikeCount(
																	 postEntity.getLikeCount());
																 postResponseDto.setHashtagContents(
																	 postEntity.getHashtagContents());
																 return postResponseDto;
															 }).collect(Collectors.toList()));

		return ResponseEntity.status(HttpStatus.OK)
							 .body(paginationResponseDto);
	}

	public ResponseEntity<PostResponseDto> getPostById(Long postId) {
		PostEntity post = postRepository.findById(postId)
										.orElseThrow(() -> new IllegalArgumentException(
											"게시글이 존재하지 않습니다")); //TODO : custom 예외처리로 리팩토링 필요

		PostResponseDto postResponseDto = PostMapper.toDto(post);

		return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
	}

}
