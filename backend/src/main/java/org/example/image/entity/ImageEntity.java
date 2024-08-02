package org.example.image.entity;

import org.example.image.storage.core.StorageType;

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
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "image")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long imageId;

	@Column(name = "storage_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private StorageType storageType;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@Builder
	protected ImageEntity(
		StorageType storageType,
		String imageUrl
	) {
		this.storageType = storageType;
		this.imageUrl = imageUrl;
	}
}
