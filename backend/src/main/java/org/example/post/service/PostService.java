package org.example.post.service;

import java.util.ArrayList;
import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	private final ImageStorageManager imageStorageManager;

	public PostDto.CreatePostDtoResponse createPost(PostDto.CreatePostDtoRequest postDto,
		String email, MultipartFile image) {
		UserEntity user = userRepository.findByEmail(email)
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

	public Page<PostDto.PostDtoResponse> findAllPosts(
		PostSearchCondition postSearchCondition, Pageable pageable
	) {
		return postRepository.search(postSearchCondition, pageable);
	}

	public PostDto.PostDetailDtoResponse getPostById(Long postId) {
		PostEntity postEntity = postRepository.findById(postId)
											  .orElseThrow(
												  () -> ApiPostException.builder()
													  .category(ApiErrorCategory.RESOURCE_UNAUTHORIZED)
													  .subCategory(ApiPostErrorSubCategory.POST_NOT_FOUND)
													  .build()
											  );

		return PostDto.PostDetailDtoResponse.toDto(postEntity);
	}
}
