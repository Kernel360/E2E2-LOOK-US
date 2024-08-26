package org.example.image.ImageAnalyzeManager.analyzer.type;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NormalizedVertex2D implements Serializable {
	private Float dx;
	private Float dy;


	@JsonCreator
	public NormalizedVertex2D(
		@JsonProperty("dx") Float dx,
		@JsonProperty("dy") Float dy
	) {
		this.dx = dx;
		this.dy = dy;
	}
}
