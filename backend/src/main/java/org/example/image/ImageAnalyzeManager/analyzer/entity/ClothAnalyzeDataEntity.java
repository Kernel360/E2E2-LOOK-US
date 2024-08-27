package org.example.image.ImageAnalyzeManager.analyzer.entity;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "cloth_analyze_data")
@Getter
@NoArgsConstructor(access = AccessLevel.NONE)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothAnalyzeDataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_analyze_data_id")
	private Long imageAnalyzeDataId;

	/**
	 * 분석한 대상 리소스 id
	 */
	@Column(name = "image_location_id", nullable = false)
	private Long imageLocationId;

	/**
	 * 어떤 옷 타입 인가? (ex. 상의, 하의, 모자)
	 */
	@Column(name = "cloth_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ClothType clothType;

	/**
	 * 옷의 상세 이름은 무엇 인가? (ex. 하이힐, 벨트, 자켓, etc...)
	 */
	@Column(name = "cloth_name", nullable = false)
	private String clothName;

	/**
	 *   RGB Color Model
	 */
	@Column(name = "rgb_color", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private RGBColor rgbColor;

	/**
	 *   Lab Color Model
	 * ---------------------
	 *   x = a ( -100~100 )
	 *   y = b ( -100~100 )
	 *   z = L ( 0~100 )
	 */
	@Column(name = "lab_color", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private LabColor labColor;

	/**
	 * 원본 이미지 에서 옷의 위치 (0 ~ 1사이의 값 - Normalized Point)
	 * Array : [ LT (LeftTop), RB (RightBottom) ]
	 *
	 *      LT
	 *       ▕▔▔▔▔▔▔▔▔▔▔▔▔▏
	 *       ▕▏           ▏
	 *       ▕▏           ▏
	 *       ▕▁▁▁▁▁▁▁▁▁▁▁▁▏
	 *                   RB
	 *
	 *    Width = rb.x - lt.x
	 *    Height = RB.y - LT.y
	 *
	 */
	@Column(name = "bounding_box", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private List<NormalizedVertex2D> boundingBox;

	@Builder
	public ClothAnalyzeDataEntity(
		ClothType clothType,
		String clothName,
		LabColor labColor,
		RGBColor rgbColor,
		List<NormalizedVertex2D> boundingBox,
		Long imageLocationId
	) {
		this.clothType = clothType;
		this.clothName = clothName;
		this.labColor = labColor;
		this.rgbColor = rgbColor;
		this.boundingBox = boundingBox;
		this.imageLocationId = imageLocationId;
	}
}
