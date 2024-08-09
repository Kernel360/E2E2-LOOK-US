// package org.example.post.domain.dto;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;
// import java.util.stream.Collectors;
//
//
// public class PaginationDto{
// 	public record PaginationDtoRequest(
// 		int page,
// 		int size,
// 		String sortField,
// 		String sortDirection,
// 		String searchHashtagList,
// 		String searchString
// 	) {
// 		public List<String> convertHashtagContents(String hashtagContents, String regex) {
// 			return Arrays.stream(hashtagContents.split(regex))
// 				.filter(s -> !s.isEmpty())
// 				.collect(Collectors.toList());
// 		}
// 	}
//
// 	public record PaginationDtoResponse(
// 		int page,
// 		int size,
// 		long totalElements,
// 		int totalPages,
// 		String searchString,
// 		List<PostDto.GetPostDtoResponse> postResponseDtoList
// 	) {
//
// 		public PaginationDtoResponse withAdditionalPosts(List<PostDto.GetPostDtoResponse> additionalPosts) {
// 			List<PostDto.GetPostDtoResponse> newList = new ArrayList<>(this.postResponseDtoList != null ? this.postResponseDtoList : Collections.emptyList());
// 			newList.addAll(additionalPosts);
// 			return new PaginationDtoResponse(page, size, totalElements, totalPages, searchString, newList);
// 		}
// 	}
//
//
// }
//
