package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;

public class ClothTypeConfig {

	public record ClothMappingTable(
		ClothType category,
		List<String> members // TODO: 이 부분은 추후에 객체로 뺄 예정 입니다. (ex. Redis로 올리는 경우)
	) {}

	static final List<ClothMappingTable> mappingTables = List.of(
		new ClothMappingTable(
			ClothType.OUTER,
			List.of(
				"Outerwear"
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.TOP,
			List.of(
				"Top"
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.PANTS,
			List.of(
				"Pants",
				"Jeans"
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.SHOE,
			List.of(
				"Shoe",
				"Sandal",
				"High heels"
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.ACCESSORY,
			List.of(
				"Shoe",
				"Sandal",
				"Belt"
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.DRESS,
			List.of(
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.SKIRT,
			List.of(
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.HAT,
			List.of(
				// TODO: 추가 해주세요
			)),
		new ClothMappingTable(
			ClothType.BAG,
			List.of(
				// TODO: 추가 해주세요
			))
	);
}
