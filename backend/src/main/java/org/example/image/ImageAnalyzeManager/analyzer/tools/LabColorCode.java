package org.example.image.ImageAnalyzeManager.analyzer.tools;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LabColorCode {
	// TODO: 삭제 예정
	// Define colors with their LAB values
	WHITE(100, 0, 0),
	BLACK(0, 0, 0),
	GRAY(50, 0, 0),
	RED(53.23f, 80.11f, 67.22f),
	DARK_RED(30, 60, 40),
	PINK(76.32f, 24.58f, 8.79f),
	ORANGE(70, 40, 60),
	YELLOW(97.14f, -21.55f, 94.48f),
	GREEN(87.73f, -86.18f, 83.18f),
	DARK_GREEN(30, -40, 20),
	LIGHT_GREEN(85, -50, 50),
	BLUE(32.30f, 79.19f, -107.86f),
	NAVY(13.2f, 21.6f, -46.2f),
	SKY_BLUE(70, -20, -30),
	PURPLE(50, 60, -60),
	VIOLET(35.23f, 80.81f, -107.90f),
	BROWN(37.58f, 45.70f, 33.71f),
	BEIGE(93.14f, -0.28f, 8.13f),
	MAGENTA(60.32f, 98.23f, -60.82f),
	CYAN(91.11f, -48.09f, -14.13f);

	private float l;
	private float a;
	private float b;

}
