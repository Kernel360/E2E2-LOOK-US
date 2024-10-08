package org.example.post.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.image.ImageAnalyzeManager.analyzer.entity.ClothAnalyzeDataEntity;
import org.example.image.ImageAnalyzeManager.analyzer.repository.ClothAnalyzeDataRepository;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.example.image.AsyncImageAnalyzePipeline;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.example.image.redis.service.ImageRedisService;
import org.example.config.log.LogExecution;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.CategoryEntity;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.LikeEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.CategoryRepository;
import org.example.post.repository.HashtagRepository;
import org.example.post.repository.LikeRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostRepositoryImpl;
import org.example.post.repository.custom.PostSearchCondition;
import org.example.post.repository.custom.UpdateScoreType;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	private final ImageRedisService imageRedisService;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	private final ImageStorageManager imageStorageManager;
	private final AsyncImageAnalyzePipeline asyncImageAnalyzePipeline;

	private final LikeRepository likeRepository;
	private final HashtagRepository hashtagRepository;
	private final ClothAnalyzeDataRepository clothAnalyzeDataRepository;
	private final CategoryRepository categoryRepository;

	@LogExecution
	public PostDto.CreatePostDtoResponse createPost(PostDto.CreatePostDtoRequest postDto, String email,
		MultipartFile image) throws IOException {
		UserEntity user = findUserByEmail(email);

		StorageSaveResult storageSaveResult = imageStorageManager.saveImage(
			image, StorageType.LOCAL_FILE_SYSTEM
		);

		// 2. run image analyze pipeline
		asyncImageAnalyzePipeline.analyze(storageSaveResult.imageLocationId());

		// 3. save post
		PostEntity post = new PostEntity(
			user,
			postDto.postContent(),
			storageSaveResult.imageLocationId(),
			0
		);
		postRepository.save(post);

		List<HashtagEntity> hashtagEntities = postDto.convertContents(postDto.hashtagContents(), "#")
			.stream()
			.map(hashtag -> new HashtagEntity(post, hashtag))
			.toList();
		// 카테고리 처리: 사용자 입력한 카테고리 중 DB에 존재하는 것만 처리
		List<String> categoryNames = postDto.convertContents(postDto.categoryContents(), ",");
		List<CategoryEntity> categoryEntities = categoryNames.stream()
			.map(categoryRepository::findByCategoryContent)
			.collect(Collectors.toList());

		hashtagRepository.saveAll(hashtagEntities);

		post.addHashtags(hashtagEntities);
		post.addCategories(categoryEntities);

		PostEntity savedPost = postRepository.save(post);

		return PostDto.CreatePostDtoResponse.toDto(savedPost);
	}

	@Transactional
	@LogExecution
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
			List<HashtagEntity> hashtagEntities = updateRequest.convertContents(updateRequest.hashtagContents(),
				"#").stream().map(hashtag -> new HashtagEntity(post, hashtag)).toList();

			hashtagRepository.saveAll(hashtagEntities);
			post.updateHashtags(hashtagEntities);
		}

		// 카테고리 수정 처리
		if (!updateRequest.categoryContents().isEmpty()) {
			List<String> newCategoryContents = updateRequest.convertContents(updateRequest.categoryContents(), ",");

			List<CategoryEntity> newCategories = newCategoryContents.stream()
				.map(categoryContent -> {
					//새로운 카테고리 내용 바탕 기존 카테고리 조회
					List<CategoryEntity> existingCategories = categoryRepository.findAllByCategoryContent(
						categoryContent);
					//존재하는 카테고리 있으면 재사용, 없으면 새로 생성하기
					if (!existingCategories.isEmpty()) {
						return existingCategories.get(0);
					} else {
						return new CategoryEntity(categoryContent);
					}
				})
				.collect(Collectors.toList());

			// 새로운 카테고리들로 갱신
			post.updateCategories(newCategories);
		}

		return PostDto.CreatePostDtoResponse.toDto(post);
	}

	@Transactional(readOnly = true)
	@LogExecution
	public Page<PostDto.PostDtoResponse> findAllPosts(PostSearchCondition postSearchCondition, Pageable pageable) {
		return postRepository.search(postSearchCondition, pageable);
	}

	@Transactional(readOnly = true)
	@LogExecution
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

	@Transactional
	@LogExecution
	public Boolean like(Long postId, String email) throws JsonProcessingException {
		PostEntity post = findPostById(postId);
		UserEntity user = findUserByEmail(email);
		Long imageLocationId = post.getImageLocationId();

		// user 는 like 을 한번 만 누를 수 있다.
		if (existLikePost(user, post)) {
			// 좋아요를 누른 상태이면, 좋아요 취소를 위해 DB 삭제
			LikeEntity currentLikePost = likeRepository.findByUserAndPost(user, post);
			asyncImageAnalyzePipeline.updateScore(
				imageLocationId,
				UpdateScoreType.LIKE_CANCEL
			);
			post.decreaseLikeCount();
			likeRepository.delete(currentLikePost);
			return false;
		} else {
			// 좋아요를 누르지 않을 경우 DB에 저장
			asyncImageAnalyzePipeline.updateScore(
				imageLocationId,
				UpdateScoreType.LIKE
			);
			post.increaseLikeCount();
			likeRepository.save(LikeEntity.toEntity(user, post));
			return true;
		}
	}

	@LogExecution
	public void viewCount(Long post_id, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		Cookie viewCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("postView")) {
					viewCookie = cookie;
					break;
				}
			}
		}

		if (viewCookie != null) {
			if (!viewCookie.getValue().contains("[" + post_id + "]")) {
				updateView(post_id);
				String newValue = viewCookie.getValue() + "_[" + post_id + "]";
				viewCookie.setValue(newValue);
				viewCookie.setPath("/");
				viewCookie.setMaxAge(60 * 60 * 24);  // 쿠키 유효기간 1일
				response.addCookie(viewCookie);
			}
		} else {
			updateView(post_id);
			Cookie newCookie = new Cookie("postView", "[" + post_id + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 60 * 24);  // 쿠키 유효기간 1일
			response.addCookie(newCookie);
		}
	}

	@Transactional
	@LogExecution
	public int updateView(Long postId) throws JsonProcessingException {
		asyncImageAnalyzePipeline.updateScore(
			findPostById(postId).getImageLocationId(),
			UpdateScoreType.VIEW
		);
		return postRepository.updateView(postId);
	}

	@Transactional(readOnly = true)
	@LogExecution
	public boolean existLikePost(UserEntity user, PostEntity post) {
		return likeRepository.existsByUserAndPost(user, post);
	}

	@Transactional(readOnly = true)
	@LogExecution
	public int likeCount(Long postId) {
		PostEntity post = findPostById(postId);

		return likeRepository.likeCount(post);
	}

	@Transactional
	@LogExecution
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

	@Transactional(readOnly = true)
	@LogExecution
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

	@Transactional(readOnly = true)
	@LogExecution
	public UserEntity findUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(
				() -> ApiUserException.builder()
					.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
					.subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
					.setErrorData(() -> ("존재하는 사용자가 없습니다" + email))
					.build());
	}

	public Page<PostDto.PostDtoResponse> findAllPostsByRGB(int[] rgbColor, Pageable pageable) throws JsonProcessingException {
		// 공통 메서드를 사용해 이미지 ID를 조회
		Set<Long> imageIdSet = findImageIdsByRGBAndSimilarColors(rgbColor);

		// 이미지 ID로 게시글을 조회하고 DTO로 변환
		List<PostDto.PostDtoResponse> postDtoResponses = findPostsByImageIds(imageIdSet);

		// 정렬 및 페이지네이션 적용
		Sort sort = pageable.getSort();
		List<PostDto.PostDtoResponse> sortedPosts = new ArrayList<>();
		if (sort.isSorted()) {
			sortedPosts = PostRepositoryImpl.sortPosts(sort, postDtoResponses, pageable);
		}

		return new PageImpl<>(sortedPosts, pageable, postDtoResponses.size());
	}

	@Transactional(readOnly = true)
	@LogExecution
	public Page<PostDto.PostDtoResponse> findAllPostsByCategory(Long categoryId, Pageable pageable) {
		// 카테고리 이름을 통해 해당 카테고리에 속한 게시글들을 조회합니다.
		List<PostEntity> posts = postRepository.findAllByCategoryId(categoryId);

		// 각 게시글을 DTO로 변환하여 반환할 리스트를 생성합니다.
		List<PostDto.PostDtoResponse> postDtoResponses = posts.stream()
			.map(postEntity -> new PostDto.PostDtoResponse(
				postEntity.getUser().getNickname(),
				postEntity.getPostId(),
				postEntity.getImageLocationId(),
				postEntity.getHashtagContents(),
				postEntity.getCategoryContents(),
				postEntity.getLikeCount(),
				postEntity.getHits(),
				postEntity.getCreatedAt()))
			.collect(Collectors.toList());

		// 페이지네이션을 적용하여 결과를 반환합니다.
		return new PageImpl<>(postDtoResponses, pageable, postDtoResponses.size());
	}

	// 카테고리 전체를 가져오는 메서드
	public List<CategoryEntity> getAllCategory() {
		return categoryRepository.findAll();
	}


	@Transactional(readOnly = true)
	public Page<PostDto.PostDtoResponse> findAllPostsByCategoryAndRGB(Long categoryId, int[] rgbColor, Pageable pageable)
		throws JsonProcessingException {

		// 1. 주어진 색상에 해당하는 이미지 ID들을 조회
		Set<Long> imageIdSet = findImageIdsByRGBAndSimilarColors(rgbColor);

		// 2. 해당 이미지 ID를 가진 게시글들을 모두 조회
		List<PostEntity> posts = new ArrayList<>();
		for (Long imageId : imageIdSet) {
			List<PostEntity> postEntities = postRepository.findAllByImageLocationId(imageId);
			posts.addAll(postEntities);
		}

		// 3. 카테고리 필터링 적용
		// 3. 카테고리 필터링 적용 - categoryId를 사용하여 필터링
		List<PostEntity> filteredPosts = posts.stream()
			.filter(post -> post.getCategories().stream()
				.anyMatch(cat -> cat.getCategoryId().equals(categoryId)))
			.collect(Collectors.toList());


		// 4. 각 게시글을 DTO로 변환하여 반환할 리스트를 생성합니다.
		List<PostDto.PostDtoResponse> postDtoResponses = filteredPosts.stream()
			.map(postEntity -> new PostDto.PostDtoResponse(
				postEntity.getUser().getNickname(),
				postEntity.getPostId(),
				postEntity.getImageLocationId(),
				postEntity.getHashtagContents(),
				postEntity.getCategoryContents(),
				postEntity.getLikeCount(),
				postEntity.getHits(),
				postEntity.getCreatedAt()))
			.collect(Collectors.toList());

		// 5. 정렬 및 페이지네이션 적용
		Sort sort = pageable.getSort();
		List<PostDto.PostDtoResponse> sortedPosts = new ArrayList<>();
		if (sort.isSorted()) {
			sortedPosts = PostRepositoryImpl.sortPosts(sort, postDtoResponses, pageable);
		}

		return new PageImpl<>(sortedPosts, pageable, postDtoResponses.size());
	}

	protected Set<Long> findImageIdsByRGBAndSimilarColors(int[] rgbColor) throws JsonProcessingException {
		// 주어진 RGB 색상에 해당하는 이미지 ID를 조회
		Set<Long> imageIdSet = clothAnalyzeDataRepository.findAllByRgbColor(new RGBColor(rgbColor[0], rgbColor[1], rgbColor[2]))
			.stream()
			.map(ClothAnalyzeDataEntity::getImageLocationId)
			.collect(Collectors.toSet());

		// 유사한 색상 목록을 조회하여 이미지 ID 추가
		List<int[]> similarColorList = imageRedisService.getCloseColorList(rgbColor, 2);
		for (int[] color : similarColorList) {
			imageIdSet.addAll(clothAnalyzeDataRepository.findAllByRgbColor(new RGBColor(color[0], color[1], color[2]))
				.stream()
				.map(ClothAnalyzeDataEntity::getImageLocationId)
				.collect(Collectors.toSet()));
		}

		return imageIdSet;
	}

	protected List<PostDto.PostDtoResponse> findPostsByImageIds(Set<Long> imageIdSet) {
		List<PostDto.PostDtoResponse> postDtoResponses = new ArrayList<>();
		for (Long imageId : imageIdSet) {
			postRepository.findAllByImageLocationId(imageId)
				.forEach(postEntity -> postDtoResponses.add(
					new PostDto.PostDtoResponse(postEntity.getUser().getNickname(), postEntity.getPostId(),
						postEntity.getImageLocationId(), postEntity.getHashtagContents(), postEntity.getCategoryContents(),
						postEntity.getLikeCount(), postEntity.getHits(), postEntity.getCreatedAt())));
		}
		return postDtoResponses;
	}
}
