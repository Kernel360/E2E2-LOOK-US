/*
package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GoogleApiCredentialRollerTest {

	private static final String MOCK_CREDENTIALS_JSON_1 = "{ \"type\": \"service_account\", \"project_id\": \"test-project-1\", \"private_key_id\": \"key-id-1\", \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nABC123\\n-----END PRIVATE KEY-----\\n\", \"client_email\": \"test-1@test-project.iam.gserviceaccount.com\", \"client_id\": \"client-id-1\", \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\", \"token_uri\": \"https://oauth2.googleapis.com/token\", \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\", \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/test-1%40test-project.iam.gserviceaccount.com\" }";
	private static final String MOCK_CREDENTIALS_JSON_2 = "{ \"type\": \"service_account\", \"project_id\": \"test-project-2\", \"private_key_id\": \"key-id-2\", \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nDEF456\\n-----END PRIVATE KEY-----\\n\", \"client_email\": \"test-2@test-project.iam.gserviceaccount.com\", \"client_id\": \"client-id-2\", \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\", \"token_uri\": \"https://oauth2.googleapis.com/token\", \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\", \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/test-2%40test-project.iam.gserviceaccount.com\" }";

	private GoogleApiCredentialRoller credentialRoller;

	@BeforeEach
	public void setup() throws IOException {
		// JSON array로 환경 변수 값 생성
		JsonArray jsonArray = new JsonArray();
		JsonObject jsonObject1 = JsonParser.parseString(MOCK_CREDENTIALS_JSON_1).getAsJsonObject();
		JsonObject jsonObject2 = JsonParser.parseString(MOCK_CREDENTIALS_JSON_2).getAsJsonObject();

		jsonArray.add(jsonObject1);
		jsonArray.add(jsonObject2);

		String jsonKeys = jsonArray.toString();

		// GoogleApiCredentialRoller 인스턴스 생성
		credentialRoller = new GoogleApiCredentialRoller(jsonKeys);
	}

	@Test
	public void testCredentialRolling() {
		// 첫 번째 자격 증명 확인
		Credentials firstCredential = credentialRoller.getCredential();
		assertNotNull(firstCredential);
		assertTrue(firstCredential instanceof GoogleCredentials);

		GoogleCredentials firstGoogleCredential = (GoogleCredentials) firstCredential;
		assertEquals("test-1@test-project.iam.gserviceaccount.com", firstGoogleCredential.getClass());

		// 두 번째 자격 증명 확인
		Credentials secondCredential = credentialRoller.getCredential();
		assertNotNull(secondCredential);
		assertTrue(secondCredential instanceof GoogleCredentials);

		GoogleCredentials secondGoogleCredential = (GoogleCredentials) secondCredential;
		assertEquals("test-2@test-project.iam.gserviceaccount.com", secondGoogleCredential.getClass());

		// 롤링하여 다시 첫 번째 자격 증명 확인
		Credentials rolledCredential = credentialRoller.getCredential();
		assertNotNull(rolledCredential);
		assertTrue(rolledCredential instanceof GoogleCredentials);

		GoogleCredentials rolledGoogleCredential = (GoogleCredentials) rolledCredential;
		assertEquals("test-1@test-project.iam.gserviceaccount.com", rolledGoogleCredential.getClass());
	}
}
*/
