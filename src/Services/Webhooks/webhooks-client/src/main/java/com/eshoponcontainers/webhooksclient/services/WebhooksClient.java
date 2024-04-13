package com.eshoponcontainers.webhooksclient.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.eshoponcontainers.webhooksclient.Settings;
import com.eshoponcontainers.webhooksclient.models.WebhookResponse;
import com.eshoponcontainers.webhooksclient.utils.AccessTokenUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebhooksClient {

    @Qualifier("grantClientWebClient")
    private final WebClient webClient;
    private final Settings settings;

    public List<WebhookResponse> loadWebhooks() {

        try {
            return WebClient.builder().baseUrl(settings.getWebhookUrl()).build().get()
                    .uri("/api/v1/webhooks")
                    .header("Authorization", "Bearer "+ AccessTokenUtils.getAccessToken())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<WebhookResponse>>() {
                    })
                    .block();

            // this.webClient.get()
            //         .uri("/api/v1/webhooks")
            //         .retrieve()
            //         .bodyToMono(new ParameterizedTypeReference<List<WebhookResponse>>() {
            //         })
            //         .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
