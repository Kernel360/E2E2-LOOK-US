package org.example.image.redis.domain.dto;

import org.example.post.repository.custom.PostPopularSearchCondition;

public class ColorDto {

	public record ColorSaveDtoRequest(

		Integer r,
		Integer g,
		Integer b,

		Integer originR,
		Integer originG,
		Integer originB
	) {

		public static ColorSaveDtoRequest update(ColorSaveDtoRequest color, Integer r, Integer g, Integer b) {
			return new ColorSaveDtoRequest(
				r, g, b,
				color.originR,
				color.originG,
				color.originB
			);
		}
	}

	public record ColorDistanceResponse(
		String name,
		double distance
	) {

	}

	public record ColorSelectedDtoRequest(
		long postId,
		double dist,
		Integer r,
		Integer g,
		Integer b,
		PostPopularSearchCondition condition
	) {

	}
	public record ColorUpdateDtoRequest(
		String name,
		Integer r,
		Integer g,
		Integer b,

		Integer originR,
		Integer originG,
		Integer originB
	) {


	}
}