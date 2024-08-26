package org.example.image.ImageAnalyzeManager.analyzer.service.GoogleImagenVisionService.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class GoogleApiCredentialRoller {

	// api key rolling index
	private int idx = 0;

	// json to credentials
	private final List<Credentials> credentialsList = new ArrayList<>();

	public static GoogleApiCredentialRoller fromEnv(String env_name) {
		return new GoogleApiCredentialRoller(env_name);
	}

	protected GoogleApiCredentialRoller(String env_name) {
		// init credentials with given env files
		JsonArray keys = JsonParser.parseString(System.getenv(env_name)).getAsJsonArray();
		keys.forEach(key -> {
			try {
				this.credentialsList.add(GoogleCredentials.fromStream(
					new ByteArrayInputStream(key.toString().getBytes())
				));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public Credentials getCredential() {
		assert (!credentialsList.isEmpty());

		Credentials credentials = this.credentialsList.get(this.idx);
		this.idx = (this.idx + 1) % (this.credentialsList.size());

		return credentials;
	}
}
