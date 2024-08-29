package org.example.config;

import java.util.Arrays;

import org.example.config.jwt.TokenProvider;
import org.example.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import org.example.config.oauth.OAuth2SuccessHandler;
import org.example.config.oauth.OAuth2UserCustomService;
import org.example.user.repository.token.RefreshTokenRepository;
import org.example.user.service.member.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebOAuthSecurityConfig {

	private final OAuth2UserCustomService oAuth2UserCustomService;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserService userService;

	@Bean
	public WebSecurityCustomizer configure() { // 스프링 시큐리티 기능 비활성화
		return (web) -> web.ignoring()
			.requestMatchers(
				new AntPathRequestMatcher("/img/**"),
				new AntPathRequestMatcher("/css/**"),
				new AntPathRequestMatcher("/js/**")
			);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // TokenAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/token").permitAll()
				.requestMatchers("/api/a1/**").permitAll()
				.requestMatchers("/api/v1/**").hasRole("USER") // hasRole method inserts ROLE_* prefix
				.requestMatchers("/api/i1/**").hasRole("ADMIN")
				.requestMatchers("/admin/**").hasRole("ADMIN") // Simple admin page, just for development
				.anyRequest().permitAll()
			)
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/login")
				.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
					.baseUri("/oauth2/authorization")
					.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
				)
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(oAuth2UserCustomService)
				)
				.successHandler(oAuth2SuccessHandler())
			)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.defaultAuthenticationEntryPointFor(
					new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
					new AntPathRequestMatcher("/api/**")
				))
			.build();
	}


	@Bean
	static RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.withDefaultRolePrefix() // spring security inserts ROLE_* prefix
								.role("ADMIN").implies("INFLUENCER")
								.role("INFLUENCER").implies("USER")
								.role("USER").implies("GUEST")
								.build();
	}

	@Bean
	public OAuth2SuccessHandler oAuth2SuccessHandler() {
		return new OAuth2SuccessHandler(tokenProvider,
			refreshTokenRepository,
			oAuth2AuthorizationRequestBasedOnCookieRepository(),
			userService
		);
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	@Bean
	public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
		return new OAuth2AuthorizationRequestBasedOnCookieRepository();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOriginPattern("*");
		// 명시적으로 도메인을 설정 (와일드카드 패턴 사용하지 않음)
		corsConfiguration.addAllowedOrigin("https://www.lookus.shop");
		corsConfiguration.addAllowedOrigin("https://api.lookus.shop");
		corsConfiguration.addAllowedOrigin("http://www.lookus.shop");
		corsConfiguration.addAllowedOrigin("http://api.lookus.shop");
		corsConfiguration.addAllowedOrigin("https://lookus.shop");
		corsConfiguration.addAllowedOrigin("http://lookus.shop");
		// corsConfiguration.addAllowedOrigin(System.getenv("API_DOMAIN"));
		// corsConfiguration.addAllowedOrigin(System.getenv("WEBSITE_DOMAIN"));
		corsConfiguration.addAllowedOrigin("http://localhost:8080"); // JUST FOR LOCAL DEV
		corsConfiguration.addAllowedOrigin("http://localhost:3000"); // JUST FOR LOCAL DEV

		// 필요한 헤더 및 메소드 추가
		corsConfiguration.addExposedHeader("Authorization");
		corsConfiguration.addExposedHeader("refresh_token");
		corsConfiguration.addExposedHeader("Set-Cookie");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Set-Cookie"));
		// 쿠키 사용을 허용 (Credentials 허용)
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
}
