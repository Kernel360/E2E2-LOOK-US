package org.example.image.resourceLocation.entity;

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
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "resource_location")
@Getter
@NoArgsConstructor(access = AccessLevel.NONE)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceLocationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "resource_location_id")
	private Long resourceLocationId;

	@Column(name = "storage_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private StorageType storageType;

	@Column(name = "saved_path", nullable = false)
	private String savedPath;

	@Builder
	protected ResourceLocationEntity(
		StorageType storageType,
		String savedPath
	) {
		this.storageType = storageType;
		this.savedPath = savedPath;
	}
}
