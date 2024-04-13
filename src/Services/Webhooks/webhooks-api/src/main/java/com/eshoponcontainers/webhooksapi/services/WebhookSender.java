package com.eshoponcontainers.webhooksapi.services;

import java.net.http.HttpClient;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eshoponcontainers.webhooksapi.entities.WebhookData;
import com.eshoponcontainers.webhooksapi.entities.WebhookSubscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookSender {

    public void sendAll(List<WebhookSubscription> receivers, WebhookData data) {
        HttpClient client = HttpClient.newHttpClient();
        receivers.stream().forEach(reciever -> client.sendAsync(null, null));
    }
}
