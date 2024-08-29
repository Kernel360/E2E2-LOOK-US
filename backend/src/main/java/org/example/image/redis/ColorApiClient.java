package org.example.image.redis;

import org.example.image.redis.domain.dto.ColorInfoResponse;
import org.example.config.log.LogExecution;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ColorApiClient {

	private static final String API_URL = "https://www.thecolorapi.com/id?hex={hex}";

	@LogExecution
	public String getColorInfo(String hexColor) {
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Exchange method for direct object mapping
			ResponseEntity<ColorInfoResponse> response = restTemplate.exchange(
				API_URL,
				HttpMethod.GET,
				null,
				ColorInfoResponse.class,
				hexColor
			);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return response.getBody().getName().getValue();
			}

			return "Color information not found";
		} catch (HttpClientErrorException e) {
			// Handle client errors (e.g., 4xx responses)
			return "Client error: " + e.getMessage();
		} catch (Exception e) {
			// Handle other errors
			return "Error: " + e.getMessage();
		}

	}
}
