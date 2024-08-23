package org.example.scrap.domain.dto;

import java.util.List;

import org.example.scrap.domain.enums.ScrapStatus;

public class ScrapDto {

	public record SetScrapStatusRequestDto(
		ScrapStatus status
	) {}

	public record GetAllPostScrapsResponseDto(
		List<Long> postId
	) {}
}
