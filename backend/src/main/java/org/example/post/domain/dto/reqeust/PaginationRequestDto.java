package org.example.post.domain.dto.reqeust;

import java.util.Optional;

import org.example.common.TimeTrackableDto;
import org.example.common.TimeTrackableEntity;
import org.example.post.domain.enums.PostStatus;

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
	private String searchHashtag;
	private String searchString;
}
