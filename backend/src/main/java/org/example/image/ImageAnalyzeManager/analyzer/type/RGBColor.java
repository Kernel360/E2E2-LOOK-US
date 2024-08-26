package org.example.image.ImageAnalyzeManager.analyzer.type;

import java.awt.*;
import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({
	"colorSpace",
	"rgb",
	"alpha",
	"transparency"
})
public class RGBColor extends Color {

	@ConstructorProperties({"red", "green", "blue"})
	@JsonCreator
	public RGBColor(
		@JsonProperty("red") int r,
		@JsonProperty("green") int g,
		@JsonProperty("blue") int b
	) {
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
