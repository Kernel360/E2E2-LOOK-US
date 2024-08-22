package org.example.image.ImageAnalyzeManager.analyzer.entity;

import java.util.List;

import org.example.image.ImageAnalyzeManager.analyzer.type.ClothType;
import org.example.image.ImageAnalyzeManager.analyzer.type.LabColor;
import org.example.image.ImageAnalyzeManager.analyzer.type.NormalizedVertex2D;
import org.example.image.ImageAnalyzeManager.analyzer.type.RGBColor;
import org.example.image.imageStorageManager.storage.entity.ResourceLocationEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@Column(name = "resouce_location_id", nullable = false)
	private Long resourceLocationId;

	/**
	 * 어떤 옷 타입 인가?
	 */
	@Column(name = "cloth_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ClothType clothType;

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
	 *   RGB Color Model
	 */
	@Column(name = "rgb_color", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private RGBColor rgbColor;

	/**
	 * 옷 이미지를 가리키는 2개의 Normalized Point
	 */
	@Column(name = "bounding_box", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private List<NormalizedVertex2D> boundingBox;

	@Builder
	public ClothAnalyzeDataEntity(
		ClothType clothType,
		LabColor labColor,
		RGBColor rgbColor,
		List<NormalizedVertex2D> boundingBox,
		Long resourceLocationId
	) {
		this.clothType = clothType;
		this.labColor = labColor;
		this.rgbColor = rgbColor;
		this.boundingBox = boundingBox;
		this.resourceLocationId = resourceLocationId;
	}
}
