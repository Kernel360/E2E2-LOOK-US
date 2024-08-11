package org.example.image.controller;

import org.example.image.storageManager.common.StorageFindResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/a1")
public class ImagePublicResourceController {

	private final ImageStorageManager imageStorageManager;

	public ImagePublicResourceController(ImageStorageManager imageStorageManager) {
		this.imageStorageManager = imageStorageManager;
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
		).body(result.resource());
	}
}
