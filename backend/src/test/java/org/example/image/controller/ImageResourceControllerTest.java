package org.example.image.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.Duration;

import org.example.config.jwt.TokenProvider;
import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.user.domain.entity.member.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
class ImageResourceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ImageStorageManager imageStorageManager;

	@Autowired
	private TokenProvider tokenProvider;

	private String jwtToken;

	@BeforeEach
	void setUp() throws Exception {
		UserEntity userEntity = UserEntity.builder()
			.password("password")
			.email("test@example.com")
			.build();

		jwtToken = tokenProvider.generateToken(userEntity, Duration.ofDays(14));
		jwtToken = "Bearer " + jwtToken;

		// Mocking the saveResource method
		StorageSaveResult mockResult = new StorageSaveResult(StorageType.LOCAL_FILE_SYSTEM, 1L);
		when(imageStorageManager.saveResource(any(MultipartFile.class), eq(StorageType.LOCAL_FILE_SYSTEM)))
			.thenReturn(mockResult);
	}

	@Test
	@DisplayName("[POST] 업로드 테스트")
	void handleImageUpload() throws Exception {
		// Sample file content
		byte[] fileContent = "sample image content".getBytes();

		// Create a MockMultipartFile instance
		MockMultipartFile file = new MockMultipartFile(
			"image",                // The name of the part
			"sample-image.png",    // The original filename
			"image/png",            // The content type
			fileContent            // The content of the file
		);

		// Perform the multipart request
		mockMvc.perform(multipart("/api/v1/image") // The URL of the endpoint
				.file(file) // Attach the file
				.header("Authorization", jwtToken)
			)
			.andExpect(status().isCreated()) // Assert the expected status code
			.andExpect(content().string("1")); // Assert the response body if needed
	}

	@Test
	void serveFile() {
		// Implement test for serveFile if needed
	}
}
