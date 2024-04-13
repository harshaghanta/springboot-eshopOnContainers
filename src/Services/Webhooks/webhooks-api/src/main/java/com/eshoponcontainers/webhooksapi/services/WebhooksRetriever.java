package com.eshoponcontainers.webhooksapi.services;

import org.springframework.stereotype.Service;
import java.util.List;

import com.eshoponcontainers.webhooksapi.entities.WebhookSubscription;
import com.eshoponcontainers.webhooksapi.entities.WebhookType;
import com.eshoponcontainers.webhooksapi.repositories.WebhookSubscritpionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebhooksRetriever {

    private final WebhookSubscritpionRepository webhookSubscritpionRepository;

    public List<WebhookSubscription> getSubscriptionsOfType(WebhookType type) {
        return webhookSubscritpionRepository.findByType(type);
    }
}
