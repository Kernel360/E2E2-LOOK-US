package org.example.image.ImageAnalyzeManager.analyzer.type;

import java.io.Serializable;

import org.example.image.ImageAnalyzeManager.analyzer.tools.LabColorConverter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Convert(converter = LabColorConverter.class)
public class LabColor implements Serializable {

	private Float l; // l (0~100)
	private Float a; // a (-100~100)
	private Float b; // b (-100~100)

	/*public LabColor(float L, float a, float b) {
		this.L = L;
		this.a = a;
		this.b = b;
	}*/

	@JsonCreator
	public LabColor(
		@JsonProperty("l") Float l,
		@JsonProperty("a") Float a,
		@JsonProperty("b") Float b
	) {
		this.l = l;
		this.a = a;
		this.b = b;
	}
}
