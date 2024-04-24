package com.eshoponcontainers.webhooksapi.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import com.eshoponcontainers.webhooksapi.entities.WebhookData;
import com.eshoponcontainers.webhooksapi.entities.WebhookSubscription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhooksSender {

    private final ObjectMapper objectMapper;

    public void sendAll(List<WebhookSubscription> receivers, WebhookData data) {
        log.info("Sending WebhookData to all receivers: {}", data);
        
        String stringData = ""; // Initialize stringData with an empty string
        try {
            stringData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String strData = stringData;
        receivers.stream().forEach(receiver -> sendData(receiver, strData));
    }

    private void sendData(WebhookSubscription subscription, String data) {

        log.info("Sending data for subscription {} to url {}", subscription, subscription.getDestUrl());

        RequestBodySpec request = WebClient.create().post()
                .uri(subscription.getDestUrl())
                .header("Content-Type", "application/json");

        if (subscription.getToken() != null && !subscription.getToken().isEmpty())
            request.header("X-eshop-whtoken", subscription.getToken());

        request
                .bodyValue(data)
                .exchange()
                .doOnError(e -> log.error("Error sending data to webhook {}", subscription.getDestUrl(), e))
                .doOnSuccess(response -> log.info("Data : {} sent to webhook url: {}", data, subscription.getDestUrl()))
                .subscribe();
    }

}
