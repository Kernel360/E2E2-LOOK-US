package org.example.post.common;

import jakarta.validation.constraints.Pattern;
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
public class SortProperty {

	@Pattern(
		regexp = "^(createdAt/likes)$",
		message = "wrong sort strategy"
	)
	private String property;

	@Pattern(
		regexp = "^(DESC/ASC)$",
		message = "Invalid sort order"
	)
	private String direction;
}
