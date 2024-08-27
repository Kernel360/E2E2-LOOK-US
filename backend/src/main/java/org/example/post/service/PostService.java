package org.example.post.service;

import java.io.IOException;
import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.image.AsyncImageAnalyzer;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.example.image.redis.service.ImageRedisService;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.LikeEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.LikeRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.repository.custom.UpdateScoreType;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	private final ImageStorageManager imageStorageManager;
	private final AsyncImageAnalyzer asyncImageAnalyzer;

	private final LikeRepository likeRepository;
	private final HashtagRepository hashtagRepository;

	public PostDto.CreatePostDtoResponse createPost(PostDto.CreatePostDtoRequest postDto,
		String email, MultipartFile image) throws IOException {
		UserEntity user = findUserByEmail(email);

		// 1. save image file
		StorageSaveResult storageSaveResult = imageStorageManager.saveImage(
			image, StorageType.LOCAL_FILE_SYSTEM
		);

		// 2. run image analyze pipeline
		asyncImageAnalyzer.run(storageSaveResult.imageLocationId());

		// 3. save post
		PostEntity post = new PostEntity(
			user,
			postDto.postContent(),
			storageSaveResult.imageLocationId(),
			0
		);
		postRepository.save(post);

		List<HashtagEntity> hashtagEntities = postDto.convertHashtagContents(postDto.hashtagContents(), "#")
			.stream()
			.map(hashtag -> new HashtagEntity(post, hashtag))
			.toList();

		hashtagRepository.saveAll(hashtagEntities);

		post.addHashtags(hashtagEntities);

		PostEntity savedPost = postRepository.save(post);

		return PostDto.CreatePostDtoResponse.toDto(savedPost);
	}

	public PostDto.CreatePostDtoResponse updatePost(
		PostDto.CreatePostDtoRequest updateRequest,
		MultipartFile image,
		String email,
		Long postId
	) throws IOException {
		UserEntity user = findUserByEmail(email);

		PostEntity post = findPostById(postId);

		// check if post is disable
		if (post.getPostStatus().equals(PostStatus.DISABLED)) {
			throw ApiPostException.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiPostErrorSubCategory.POST_DISABLED)
				.build();
		}

		// check user is right author of post
		if (!post.getUser().getUserId().equals(user.getUserId())) {
			throw ApiPostException.builder()
				.category(ApiErrorCategory.RESOURCE_UNAUTHORIZED)
				.subCategory(ApiPostErrorSubCategory.POST_INVALID_AUTHOR)
				.build();
		}

		if (!image.isEmpty()) {
			StorageSaveResult storageSaveResult = imageStorageManager.saveImage(
				image,
				StorageType.LOCAL_FILE_SYSTEM
			);
			post.updateImage(storageSaveResult.imageLocationId());
		}

		if (!updateRequest.postContent().isEmpty()) {
			post.updatePostContent(updateRequest.postContent());
		}

		if (!updateRequest.hashtagContents().isEmpty()) {
			List<HashtagEntity> hashtagEntity = hashtagRepository.findAllByPost(post);
			for (HashtagEntity he : hashtagEntity) {
				hashtagRepository.deleteById(he.getHashtagId());
			}
			List<HashtagEntity> hashtagEntities = updateRequest.convertHashtagContents(updateRequest.hashtagContents(),
					"#")
				.stream()
				.map(hashtag -> new HashtagEntity(post, hashtag))
				.toList();

			hashtagRepository.saveAll(hashtagEntities);
			post.updateHashtags(hashtagEntities);
		}

		return PostDto.CreatePostDtoResponse.toDto(post);
	}

	@Transactional(readOnly = true)
	public Page<PostDto.PostDtoResponse> findAllPosts(
		PostSearchCondition postSearchCondition, Pageable pageable
	) {
		return postRepository.search(postSearchCondition, pageable);
	}

	@Transactional(readOnly = true)
	public PostDto.PostDetailDtoResponse getPostById(Long postId) {
		PostEntity post = findPostById(postId);

		String email = null;
		boolean existLikePost = false;

		// 인증 정보가 있는지 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			email = authentication.getName();
		}

		// 회원인 경우 좋아요 상태 확인
		if (email != null && !email.equals("anonymousUser")) {
			UserEntity user = findUserByEmail(email);
			existLikePost = existLikePost(user, post);
		}

		return PostDto.PostDetailDtoResponse.toDto(post, existLikePost);
	}

	public Boolean like(Long postId, String email) throws JsonProcessingException {
		PostEntity post = findPostById(postId);
		UserEntity user = findUserByEmail(email);
		Long imageLocationId = post.getImageLocationId();

		// user 는 like 을 한번 만 누를 수 있다.
		if (existLikePost(user, post)) {
			// 좋아요를 누른 상태이면, 좋아요 취소를 위해 DB 삭제
			LikeEntity currentLikePost = likeRepository.findByUserAndPost(user, post);
			asyncImageAnalyzer.updateScore(
				imageLocationId,
				UpdateScoreType.LIKE_CANCEL
			);
			post.decreaseLikeCount();
			likeRepository.delete(currentLikePost);
			return false;
		} else {
			// 좋아요를 누르지 않을 경우 DB에 저장
			asyncImageAnalyzer.updateScore(
				imageLocationId,
				UpdateScoreType.LIKE
			);
			post.increaseLikeCount();
			likeRepository.save(LikeEntity.toEntity(user, post));
			return true;
		}
	}

	public void viewCount(Long post_id, HttpServletRequest request, HttpServletResponse response) throws
		JsonProcessingException {
		Cookie oldCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("postView")) {
					oldCookie = cookie;
				}
			}
		}

		if (oldCookie != null) {
			if (!oldCookie.getValue().contains("["+ post_id.toString() +"]")) {
				updateView(post_id);
				oldCookie.setValue(oldCookie.getValue() + "_[" + post_id + "]");
				oldCookie.setPath("/");
				oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
				response.addCookie(oldCookie);
			}
		} else {
			updateView(post_id);
			Cookie newCookie = new Cookie("postView", "[" + post_id + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
			response.addCookie(newCookie);
		}
	}

	public int updateView(Long postId) throws JsonProcessingException {
		asyncImageAnalyzer.updateScore(
			findPostById(postId).getImageLocationId(),
			UpdateScoreType.VIEW
		);
		return postRepository.updateView(postId);
	}

	@Transactional(readOnly = true)
	public boolean existLikePost(UserEntity user, PostEntity post) {
		return likeRepository.existsByUserAndPost(user, post);
	}

	public int likeCount(Long postId) {
		PostEntity post = findPostById(postId);

		return likeRepository.likeCount(post);
	}

	public void delete(Long postId, String email) {
		UserEntity user = findUserByEmail(email);

		PostEntity post = findPostById(postId);

		if (!user.getUserId().equals(post.getUser().getUserId())) {
			throw ApiPostException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiPostErrorSubCategory.POST_INVALID_AUTHOR)
				.build();
		}

		likeRepository.deleteAllByPost(post);
		postRepository.delete(post);
	}

	public PostEntity findPostById(Long postId) {

		return postRepository.findById(postId)
			.orElseThrow(
				() -> ApiPostException.builder()
					.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
					.subCategory(ApiPostErrorSubCategory.POST_NOT_FOUND)
					.setErrorData(() -> ("잘못된 게시글 조회 요청입니다."))
					.build()
			);

	}

	public UserEntity findUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(
				() -> ApiUserException.builder()
					.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
					.subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
					.setErrorData(() -> ("존재하는 사용자가 없습니다" + email))
					.build()
			);
	}
}
