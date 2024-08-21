package org.example.image.ImageAnalyzeManager.analyzer.type;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NormalizedVertex2D implements Serializable {
	private Float dx;
	private Float dy;
}
