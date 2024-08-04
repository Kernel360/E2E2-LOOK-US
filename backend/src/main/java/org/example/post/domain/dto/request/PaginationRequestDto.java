package org.example.post.domain.dto.request;

import java.util.List;

import org.example.common.TimeTrackableDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto extends TimeTrackableDto {
	private int page;
	private int size;
	private String sortField;
	private String sortDirection;
	private List<String> searchHashtagList;
	private String searchString;
}
