package org.example.image.test;

import org.example.image.storageManager.ImageStorageManager;
import org.example.image.storageManager.core.StorageFindResult;
import org.example.image.storageManager.core.StorageSaveResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import lombok.RequiredArgsConstructor;

// https://spring.io/guides/gs/uploading-files
// https://www.baeldung.com/java-thymeleaf-image
// NOTE: 테스트용 컨트롤러입니다. 추후 삭제 예정입니다.

@RestController
public class ImageTestController {

	private final ImageStorageManager imageStorageManager;

	public ImageTestController(ImageStorageManager imageStorageManager) {
		this.imageStorageManager = imageStorageManager;
	}

	@PostMapping("/image")
	public ResponseEntity<Long> handleImageUpload(
		@RequestParam("image") MultipartFile file
	) {
		StorageSaveResult result = this.imageStorageManager.saveResource(file);

		return ResponseEntity.status( HttpStatus.CREATED )
							 .body( result.resourceLocationId() );
	}

	@GetMapping("/image/{resourceId}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(
		@PathVariable Long resourceId
	) {
		StorageFindResult result = this.imageStorageManager.findResourceById(resourceId);

		return ResponseEntity.ok().header(
			HttpHeaders.CONTENT_DISPOSITION,
			"attachment; filename=\"" + result.resource().getFilename() + "\""
		).body( result.resource() );
	}
}
