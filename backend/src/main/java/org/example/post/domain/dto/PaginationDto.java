package org.example.post.domain.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;

public class PaginationDto{
	public record PaginationDtoRequest(
		int page,
		int size,
		String sortField,
		String sortDirection,
		String searchHashtagList,
		String searchString
	) {
		public List<String> convertHashtagContents(String hashtagContents, String regex) {
			return Arrays.stream(hashtagContents.split(regex))
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
		}
	}

	public record PaginationDtoResponse(
		int page,
		int size,
		long totalElements,
		int totalPages,
		String searchString,
		List<PostDto.GetPostDtoResponse> postResponseDtoList
	) {

		public void postResponseDtoList(List<PostDto.GetPostDtoResponse> list) {
			postResponseDtoList.addAll(list);
		}
	}


}

