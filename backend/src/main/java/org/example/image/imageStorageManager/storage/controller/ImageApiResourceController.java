package org.example.image.imageStorageManager.storage.controller;

import java.io.IOException;

import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author minkyeu kim
 * @apiNote
 * 현재는 이미지 서비스가 어플리케이션에서 구동됩니다.
 * 그러나 추후 별도 이미지 서버를 분리할 예정입니다.
 * @see
 * <li>
 * <a href="https://www.microsoft.com/en-us/research/publication/to-blob-or-not-to-blob-large-object-storage-in-a-database-or-a-filesystem/?from=https://research.microsoft.com/apps/pubs/default.aspx?id=64525&type=exact">
 *      마이크로소프트 블로그 : 파일 저장시 파일시스템 vs 데이터베이스
 * </a>
 * </li>
 * <li>
 * <a href="https://stackoverflow.com/questions/913208/pros-and-cons-of-a-separate-image-server-e-g-images-mydomain-com">
 *     Pros and Cons of a seprate Image-Server
 * </a>
 * </li>
 */
@RestController
@RequestMapping("/api/v1")
public class ImageApiResourceController {

	private final ImageStorageManager imageStorageManager;

	public ImageApiResourceController(ImageStorageManager imageStorageManager) {
		this.imageStorageManager = imageStorageManager;
	}

	@PostMapping("/image")
	public ResponseEntity<Long> handleImageUpload(
		@RequestParam("image") MultipartFile file
	) throws IOException {
		StorageSaveResult result = this.imageStorageManager.saveResource(file, StorageType.LOCAL_FILE_SYSTEM);

		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(result.resourceLocationId());
	}
}
