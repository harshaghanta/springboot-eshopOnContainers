package com.eshoponcontainers.webhooksapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	@Value("${oauthIssuerUrl}")
	private String oauthIssuerUrl;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(HttpMethod.OPTIONS).permitAll()
						.requestMatchers("/actuator/**").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer((oauth2) -> oauth2
						.jwt(Customizer.withDefaults()));
		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		// log.info("IssuerUrl:{}", oauthIssuerUrl);
		// return JwtDecoders.fromIssuerLocation(oauthIssuerUrl);
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