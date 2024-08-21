package org.example.image.ImageAnalyzeManager.analyzer.type;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LabColor implements Serializable {

	private Float L; // L (0~100)
	private Float a; // a (-100~100)
	private Float b; // b (-100~100)

	public LabColor(float L, float a, float b) {
		this.L = L;
		this.a = a;
		this.b = b;
	}
}
