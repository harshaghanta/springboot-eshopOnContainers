package com.eshoponcontainers.webhooksclient.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.eshoponcontainers.webhooksclient.SecretUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	@Autowired
	private Environment env;

	@Autowired
	private SecretUtil secretUtil;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(
						c -> c.requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
								.requestMatchers("/checkpost").permitAll()
								.requestMatchers("/check").permitAll()
								.requestMatchers("/webhook-received").permitAll()
								.requestMatchers("/").permitAll()
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.anyRequest().authenticated())
				.csrf(CsrfConfigurer::disable)
				// .sessionManagement(management ->
				// management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2Login(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {

		String issuerUrl = Objects.requireNonNull(env.getProperty("ISSUER_URL"), "ISSUER_URL is not set");
		String clientId = Objects.requireNonNull(env.getProperty("CLIENT_ID"), "CLIENT_ID is not set");
		String grantType = Objects.requireNonNull(env.getProperty("AUTHORIZATION_GRANT_TYPE"),
				"AUTHORIZATION_GRANT_TYPE is not set");
		String scopes = Objects.requireNonNull(env.getProperty("AUTHORIZATION_SCOPES"),
				"AUTHORIZATION_SCOPES is not set");
		String redirectUri = Objects.requireNonNull(env.getProperty("REDIRECT_URI"), "REDIRECT_URI is not set");

		ClientRegistration registration = ClientRegistrations
				.fromIssuerLocation(issuerUrl)
				.registrationId("keycloak")
				.clientId(clientId)
				.clientSecret(secretUtil.readSecret("CLIENT_SECRET"))
				.authorizationGrantType(new AuthorizationGrantType(grantType))
				.scope(scopes.split(","))
				.redirectUri(redirectUri + "/login/oauth2/code/keycloak")
				.userNameAttributeName("preferred_username")
				.build();

		return new InMemoryClientRegistrationRepository(registration);
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
		String issuerUrl = env.getProperty("ISSUER_URL");
		// return JwtDecoders.fromIssuerLocation(oauthIssuerUrl);
		String jwkSetUri = issuerUrl + "/protocol/openid-connect/certs";

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