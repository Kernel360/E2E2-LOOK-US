package org.example.post.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.post.domain.dto.PaginationDto;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.PostRepository;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	private final ImageStorageManager imageStorageManager;

	@Transactional
	public PostDto.CreatePostDtoResponse createPost(PostDto.CreatePostDtoRequest postDto,
		String name, MultipartFile image
	) {
		UserEntity user = userRepository.findByUsername(name)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		StorageSaveResult storageSaveResult = imageStorageManager.saveResource(image,
			StorageType.LOCAL_FILE_SYSTEM);

		PostEntity postEntity = new PostEntity(
			user,
			postDto.postContent(),
			storageSaveResult.resourceLocationId(),
			new ArrayList<>()
		);

		PostEntity savedPost = postRepository.save(postEntity);

		// hashtagContents split by "#" and save postId and hashtagContent to HashtagEntity
		List<HashtagEntity> hashtags = new ArrayList<>();
		for (String hashtagString : postDto.convertHashtagContents(postDto.hashtagContents(), "#")) {
			HashtagEntity savedHashtag = new HashtagEntity(postEntity, hashtagString);
			hashtags.add(savedHashtag);
			hashtagRepository.save(savedHashtag);
		}
		// PostEntity에 HashtagEntity 리스트 설정
		postEntity.setHashtags(hashtags);

		postRepository.save(postEntity);

		return PostDto.CreatePostDtoResponse.toDto(savedPost);
	}

	public PaginationDto.PaginationDtoResponse getAllPostsOrderedBySortStrategy(
		PaginationDto.PaginationDtoRequest paginationRequestDto
	) {

		// get information for pagination by DTO
		int page = paginationRequestDto.page();
		int size = paginationRequestDto.size();
		String searchString = paginationRequestDto.searchString();
		String direction = paginationRequestDto.sortDirection();
		String field = paginationRequestDto.sortField();
		List<String> searchHashtagList = new ArrayList<>();
		if(paginationRequestDto.searchHashtagList() != null){
			searchHashtagList = paginationRequestDto.convertHashtagContents(
				paginationRequestDto.searchHashtagList(), "#");
		}

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
			postPage = postRepository.findAllByPostContentContainingAndPostStatus(
				searchString, PostStatus.PUBLISHED, pageable
			);
		}

		// search posts containing hashtag content
		// hashtagList not null
		if (searchHashtagList != null && !searchHashtagList.isEmpty()) {
			postPage = postRepository.findAllByPostStatusAndHashtags_HashtagContentIn(
				PostStatus.PUBLISHED, searchHashtagList, pageable
			);
		}

		// search posts containing both searchString and hashtag content
		// get only some posts which must have both elements
		if (searchHashtagList != null && searchString != null && !searchHashtagList.isEmpty()
			&& !searchString.isBlank()) {
			postPage = postRepository.findAllByPostContentContainingAndHashtagsContainingAndPostStatus(
				searchString, searchHashtagList, PostStatus.PUBLISHED, pageable
			);
		}

		// find no post about search condition
		if (postPage == null) {
			throw new NullPointerException("No posts found");
		}

		// DTO for return data
		PaginationDto.PaginationDtoResponse paginationResponseDto = new PaginationDto.PaginationDtoResponse(
			page,
			size,
			postPage.getTotalElements(),
			postPage.getTotalPages(),
			searchString,
			null
		);

		List<PostDto.GetPostDtoResponse> postResponseDtos = postPage.stream()
			.map(PostDto.GetPostDtoResponse::toDto).toList();
		return paginationResponseDto.withAdditionalPosts(postResponseDtos);
	}

	public PostDto.GetPostDtoResponse getPostById(Long postId) {
		PostEntity postEntity = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다")); //TODO : custom 예외처리로 리팩토링 필요

		return PostDto.GetPostDtoResponse.toDto(postEntity);

	}

}
