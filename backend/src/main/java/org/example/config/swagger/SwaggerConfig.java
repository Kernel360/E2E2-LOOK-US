package org.example.config.swagger;

import java.util.ArrayList;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
	info = @Info(
		title = "My Restful Service API 명세서",
		description = "SpringBoot로 개발하는 RESTfulAPI 명세서 입니다.",
		version = "v1.0.0"
	),
	security = {
		@SecurityRequirement(name = "bearerToken"),
		@SecurityRequirement(name = "google_oauth2")
	}
)
@SecuritySchemes({
	@SecurityScheme(
		name = "bearerToken",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
	)
})
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
	public SwaggerConfig(MappingJackson2HttpMessageConverter converter) {
		var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "octet-stream"));
		converter.setSupportedMediaTypes(supportedMediaTypes);
	}

	@Bean
	public GroupedOpenApi customTestOpenAPI() {
		String[] paths = {"/api/token", "/api/v1/*"};

		return GroupedOpenApi.builder()
			.group("사용자를 위한 API")
			.pathsToMatch(paths)
			.build();
	}
}
