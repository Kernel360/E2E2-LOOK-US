package org.example.image.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

public class Base64Converter {

	public static String convertToBase64(MultipartFile file) throws IOException {

		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		bas.write(file.getBytes());

		// Encode to Base64
		String base64 = Base64.getEncoder().encodeToString(bas.toByteArray());

		// Remove new line characters
		return base64.replaceAll("\n", "");	}
}
