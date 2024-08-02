package org.example.post.domain.dto.reqeust;

import java.util.List;

import org.example.post.common.SortProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortRequestDto {
	private SortProperty sortProperty;
}
