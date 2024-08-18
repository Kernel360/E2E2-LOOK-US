package org.example.image.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class Base64ConverterTest {

	@InjectMocks
	private Base64Converter base64Converter;

	@Mock
	private MultipartFile mockMultipartFile;

	private byte[] imageBytes;
	private String base64String;

	@BeforeEach
	public void setUp() {
		// Example image bytes and expected base64 string
		imageBytes = "test image content".getBytes();
		base64String = Base64.getEncoder().encodeToString(imageBytes);
	}

	@Test
	public void testConvertToBase64() throws IOException {
		// Arrange
		when(mockMultipartFile.getBytes()).thenReturn(imageBytes);

		// Act
		String result = base64Converter.convertToBase64(mockMultipartFile);

		// Assert
		assertEquals(base64String, result);
	}
}
