package org.example.image.ImageAnalyzeManager.analyzer.tools;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// For serialize and deserialize LAB Color entity
@Converter
public class LabColorConverter implements AttributeConverter<LabColor, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(LabColor labColor) {
		try {
			return objectMapper.writeValueAsString(labColor);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting LabColor to JSON", e);
		}
	}

	@Override
	public LabColor convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, LabColor.class);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting JSON to LabColor", e);
		}
	}
}
