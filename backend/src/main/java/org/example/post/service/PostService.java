package org.example.post.service;

import java.util.Objects;
import java.util.stream.Collectors;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.LikeEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.LikeRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	private final ImageStorageManager imageStorageManager;
	private final LikeRepository likeRepository;

	public PostDto.CreatePostDtoResponse createPost(PostDto.CreatePostDtoRequest postDto,
		String email, MultipartFile image) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		StorageSaveResult storageSaveResult = imageStorageManager.saveResource(image,
			StorageType.LOCAL_FILE_SYSTEM);

		PostEntity post = new PostEntity(
			user,
			postDto.postContent(),
			storageSaveResult.resourceLocationId(),
			0
		);

		post.addHashtags(postDto.convertHashtagContents(postDto.hashtagContents(), "#").stream()
			.map(hashtag -> new HashtagEntity(post, hashtag))
			.collect(Collectors.toList()));

		PostEntity savedPost = postRepository.save(post);

		return PostDto.CreatePostDtoResponse.toDto(savedPost);
	}

	@Transactional(readOnly = true)
	public Page<PostDto.PostDtoResponse> findAllPosts(
		PostSearchCondition postSearchCondition, Pageable pageable
	) {
		return postRepository.search(postSearchCondition, pageable);
	}

	@Transactional(readOnly = true)
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

	public Boolean like(Long postId, String email) {
		PostEntity post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다")); //TODO : custom 예외처리로 리팩토링 필요

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// user 는 like 을 한번 만 누를 수 있다.
		if (existLikePost(user, post)) {
			// 좋아요를 누른 상태이면, 좋아요 취소를 위해 DB 삭제
			LikeEntity currentLikePost = likeRepository.findByUserAndPost(user, post);
			post.decreaseLikeCount();
			likeRepository.delete(currentLikePost);
			return false;
		} else {
			// 좋아요를 누르지 않을 경우 DB에 저장
			post.increaseLikeCount();
			likeRepository.save(LikeEntity.toEntity(user, post));
			return true;
		}

		// TODO : 위에 방법으로 대체
		// likeRepository.findByUserAndPost(user, post).ifPresent(it -> {
		// 	throw new IllegalArgumentException("사용자가 이미 좋아요를 눌렀습니다");
		// });

	}

	@Transactional(readOnly = true)
	public boolean existLikePost(UserEntity user, PostEntity post) {
		return likeRepository.existsByUserAndPost(user, post);
	}

	public int likeCount(Long postId) {
		PostEntity post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다")); //TODO : custom 예외처리로 리팩토링 필요

		return likeRepository.likeCount(post);
	}

	public void delete(Long postId, String email) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
		PostEntity post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no post"));
		if (!Objects.equals(post.getUser().getUserId(), user.getUserId())) {
			throw new IllegalArgumentException("User does not match");
		}
		likeRepository.deleteAllByPost(post);
		postRepository.delete(post);
	}
}
