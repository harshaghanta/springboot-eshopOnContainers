package com.eshoponcontainers.webhooksapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	@Autowired
	private Environment env;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(
				    "/v3/api-docs",
				    "/v3/api-docs/**",
				    "/api-docs",
				    "/api-docs/**",
				    "/api-docs/swagger-config",
				    "/swagger-ui",
				    "/swagger-ui/**",
				    "/swagger-ui.html",
				    "/swagger-ui/index.html"
				).permitAll()
				.anyRequest().authenticated())
			.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.oauth2ResourceServer((oauth2) -> oauth2
				.jwt(Customizer.withDefaults()));
		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {

		String allowedCorsOrigin = env.getProperty("ALLOWED_ORIGINS");
		String allowedHeaders = env.getProperty("ALLOWED_HEADERS", "*");
		String allowedMethods = env.getProperty("ALLOWED_METHODS", "*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);

		config.addAllowedOrigin(allowedCorsOrigin);
		config.addAllowedHeader(allowedHeaders);
		config.addAllowedMethod(allowedMethods);
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public JwtDecoder jwtDecoder(Environment env) {
		// log.info("IssuerUrl:{}", oauthIssuerUrl);
		// return JwtDecoders.fromIssuerLocation(oauthIssuerUrl);
		String oauthIssuerUrl = env.getProperty("ISSUER_URL");
		String jwkSetUri = oauthIssuerUrl + "/protocol/openid-connect/certs";

		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	private static class CustomJwtDecoder implements JwtDecoder {

		private final JwtDecoder jwtDecoder;

		public CustomJwtDecoder(JwtDecoder jwtDecoder) {
			this.jwtDecoder = jwtDecoder;
		}

		@Override
		public Jwt decode(String token) throws JwtException {
			Jwt jwt = jwtDecoder.decode(token);
			return jwt;
		}

	}
}