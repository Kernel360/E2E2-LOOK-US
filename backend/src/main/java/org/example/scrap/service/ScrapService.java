package org.example.scrap.service;

import java.util.List;
import java.util.Optional;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.PostRepository;
import org.example.scrap.domain.entity.ScrapEntity;
import org.example.scrap.repository.ScrapRepository;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ScrapService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final ScrapRepository scrapRepository;

	/**
	 * 사용자가 스크랩한 모든 게시글 Id 리스트를 반환합니다.
	 */
	public List<Long> getAllScrapedPostIdByUserEmail(String email) {
		return scrapRepository.findAllByUser_Email(email).stream()
				   .map(ScrapEntity::getId)
				   .toList();
	}

	public void scrapPostByPostId(Long postId, String userEmail) {
		Optional<ScrapEntity> scrap = scrapRepository.findByPost_PostIdAndUser_Email(postId, userEmail);

		if (scrap.isPresent()) {
			// 스크랩 요청 중복은 프론트에서 부터 처리되어야 합니다.
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiUserErrorSubCategory.USER_SCRAP_DUPLICATION)
				.build();
		}

		scrapRepository.save(
			ScrapEntity.builder()
				.post(this.findPost(postId))
				.user(this.findUserByEmail(userEmail))
				.build()
		);
	}

	public void unscrapPostByPostId(Long postId, String userEmail) {
		scrapRepository.deleteByPostAndUser(
			this.findPost(postId),
			this.findUserByEmail(userEmail)
		);
	}

	private PostEntity findPost(Long postId) {
		return postRepository.findById(postId)
							 .orElseThrow(
								 () -> ApiPostException
									 .builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiPostErrorSubCategory.POST_NOT_FOUND)
									 .build()
							 );
	}

	private UserEntity findUserByEmail(String email) {
		return userRepository.findByEmail(email)
							 .orElseThrow(
								 () -> ApiUserException.builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
									 .build()
							 );
	}
}
