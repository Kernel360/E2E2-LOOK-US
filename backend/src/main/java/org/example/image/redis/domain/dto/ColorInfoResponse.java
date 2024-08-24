package org.example.image.redis.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields in JSON
public class ColorInfoResponse {

	@JsonProperty("name")
	private Name name;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Name {
		@JsonProperty("value")
		private String value;
	}
}