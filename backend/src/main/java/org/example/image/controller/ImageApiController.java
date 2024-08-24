package org.example.image.controller;

import java.util.ArrayList;
import java.util.List;

import org.example.image.ImageAnalyzeManager.ImageAnalyzeManager;
import org.example.image.ImageAnalyzeManager.type.ImageAnalyzeData;
import org.example.image.redis.service.ImageRedisService;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.PostRepository;
import org.example.post.repository.custom.PostPopularSearchCondition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/i1/images")
@RequiredArgsConstructor
public class ImageApiController {
	private final ImageRedisService imageRedisService;
	// TODO: 추후에 ROLE 설정, POST 및 request body 등 전체적인 수정 필요

	@PatchMapping("")
	public ResponseEntity<List<String>> updateColorToRedis(
		@RequestBody PostPopularSearchCondition postPopularSearchCondition
	) throws JsonProcessingException {
		List<String> updatedColorNames = imageRedisService.updateExistingColor(postPopularSearchCondition);

		return ResponseEntity.status(HttpStatus.OK).body(updatedColorNames);
	}

}
