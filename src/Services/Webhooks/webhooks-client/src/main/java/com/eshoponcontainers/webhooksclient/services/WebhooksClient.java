package com.eshoponcontainers.webhooksclient.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.eshoponcontainers.webhooksclient.models.WebhookResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebhooksClient {

    @Qualifier("grantClientWebClient")
    private final WebClient webClient;

    public List<WebhookResponse> loadWebhooks() {

        try {
            return webClient.get()
                    .uri("/api/v1/webhooks")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<WebhookResponse>>() {
                    })
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
