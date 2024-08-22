package org.example.image.ImageAnalyzeManager.analyzer.type;

import java.awt.*;
import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({
	"colorSpace",
	"rgb",
	"alpha",
	"transparency"
})
public class RGBColor extends Color {

	@ConstructorProperties({"red", "green", "blue"})
	public RGBColor(int r, int g, int b) {
		super( r, g, b );
	}

	public int getRed() {
		return super.getRed();
	}

	public int getGreen() {
		return super.getGreen();
	}

	public int getBlue() {
		return super.getBlue();
	}
}
