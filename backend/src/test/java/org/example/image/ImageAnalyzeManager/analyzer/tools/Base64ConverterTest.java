package org.example.image.ImageAnalyzeManager.analyzer.tools;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class Base64ConverterTest {
	// Convert a small text file to Base64
	@Test
	public void test_convert_small_text_file_to_base64() throws IOException {
		String content = "Hello, World!";
		MultipartFile file = new MockMultipartFile("file", content.getBytes());
		String expectedBase64 = Base64.getEncoder().encodeToString(content.getBytes());

		String result = Base64Converter.convertToBase64(file);

		assertEquals(expectedBase64, result);
	}

	// Handle IOException when file cannot be read
	@Test
	public void test_handle_ioexception_when_file_cannot_be_read() {
		MultipartFile file = new MockMultipartFile("file", new byte[0]) {
			@Override
			public byte[] getBytes() throws IOException {
				throw new IOException("Cannot read file");
			}
		};

		assertThrows(IOException.class, () -> {
			Base64Converter.convertToBase64(file);
		});
	}
}