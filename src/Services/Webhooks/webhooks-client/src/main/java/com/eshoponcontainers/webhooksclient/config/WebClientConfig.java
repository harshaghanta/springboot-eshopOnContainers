package com.eshoponcontainers.webhooksclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.eshoponcontainers.webhooksclient.Settings;
import com.eshoponcontainers.webhooksclient.filters.WebClientAuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final Settings settings;

    @Bean
    public WebClient grantClientWebClient() {
        WebClient.Builder builder = WebClient.builder();
        builder.baseUrl(settings.getWebhookUrl());

        builder.filter(new WebClientAuthorizationFilter());
        return builder.build();
    }
}
