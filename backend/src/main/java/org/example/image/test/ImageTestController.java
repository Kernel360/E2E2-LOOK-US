package org.example.image.test;

import org.example.image.service.ImageService;
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
@RequiredArgsConstructor
public class ImageTestController {

	private final ImageService imageService;

	@PostMapping("/image")
	public ResponseEntity<Long> handleImageUpload(
		@RequestParam("image") MultipartFile file
	) {
		Long imageId = this.imageService.saveImageFile(file);
		return ResponseEntity.status(HttpStatus.CREATED).body(imageId);
	}

	@GetMapping("/image/{imageId}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(
		@PathVariable Long imageId
	) {
		Resource file = this.imageService.getImageResourceById(imageId);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok()
							 .header(
								 HttpHeaders.CONTENT_DISPOSITION,
								 "attachment; filename=\"" + file.getFilename() + "\"")
							 .body(file);
	}
}
