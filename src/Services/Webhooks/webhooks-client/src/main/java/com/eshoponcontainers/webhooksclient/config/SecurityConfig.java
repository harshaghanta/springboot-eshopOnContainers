package com.eshoponcontainers.webhooksclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

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
				.authorizeHttpRequests(										
						c -> c.requestMatchers("/checkpost").permitAll()
								.requestMatchers("/check").permitAll()
								.requestMatchers("/webhook-received").permitAll()
								.requestMatchers("/actuator/**").permitAll()
								.requestMatchers("/").permitAll()
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.anyRequest().authenticated())
				.csrf(CsrfConfigurer::disable)
				// .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2Login(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
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