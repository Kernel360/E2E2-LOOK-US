package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils;

import java.util.Optional;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;

public class ClothTypeMapper {

	public static Optional<ClothType> toCategory(String tag) {
		for (var mappingTable : ClothTypeConfig.mappingTables) {
			if (mappingTable.members().contains(tag)) {
				return Optional.of(mappingTable.category());
			}
		}
		return Optional.empty();
	}
}
