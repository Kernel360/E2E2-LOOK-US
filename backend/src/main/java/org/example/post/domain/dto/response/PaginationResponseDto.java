package org.example.post.domain.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponseDto {
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private List<PostResponseDto> postCreateResponseDtoList;

}
