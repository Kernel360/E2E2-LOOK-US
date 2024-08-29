package org.example.scrap.controller;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.post.ApiPostErrorSubCategory;
import org.example.exception.post.ApiPostException;
import org.example.config.log.LogExecution;
import org.example.scrap.domain.dto.ScrapDto;
import org.example.scrap.service.ScrapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

/**
 * TODO: 영래님이 작성하신 user/me api로 이관될 예정입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/me/scraps")
public class ScrapController {
	private final ScrapService scrapService;

	@LogExecution
	@PostMapping("/posts/{post_id}")
	public ResponseEntity<?> setPostScrapStatus(
		@PathVariable("post_id") Long postId,
		@RequestBody ScrapDto.SetScrapStatusRequestDto scrapRequest
	) throws JsonProcessingException {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		switch (scrapRequest.status()) {
			case SCRAP -> scrapService.scrapPostByPostId(postId, userEmail);
			case UNSCRAP -> scrapService.unscrapPostByPostId(postId, userEmail);

			default -> throw ApiPostException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiPostErrorSubCategory.POST_INVALID_SCRAP_STATUS)
				.build();
		}

		return ResponseEntity.ok().build();
	}

	@LogExecution
	@GetMapping("/posts")
	public ResponseEntity<ScrapDto.GetAllPostScrapsResponseDto> getMyScrapedPosts() {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		return ResponseEntity.status(HttpStatus.OK)
							 .body(scrapService.getAllScrapedPostIdByUserEmail(userEmail));
	}
}
