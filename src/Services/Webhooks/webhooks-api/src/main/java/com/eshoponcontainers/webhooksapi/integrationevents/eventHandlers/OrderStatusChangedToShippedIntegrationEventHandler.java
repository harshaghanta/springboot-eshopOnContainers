package com.eshoponcontainers.webhooksapi.integrationevents.eventHandlers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.webhooksapi.entities.WebhookData;
import com.eshoponcontainers.webhooksapi.entities.WebhookSubscription;
import com.eshoponcontainers.webhooksapi.entities.WebhookType;
import com.eshoponcontainers.webhooksapi.integrationevents.events.OrderStatusChangedToShippedIntegrationEvent;
import com.eshoponcontainers.webhooksapi.services.WebhooksRetriever;
import com.eshoponcontainers.webhooksapi.services.WebhooksSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedToShippedIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToShippedIntegrationEvent> {

    private final WebhooksRetriever webhooksRetriever;
    private final WebhooksSender webhooksSender;

    @Override
    public Runnable handle(OrderStatusChangedToShippedIntegrationEvent event) {
        Runnable runnable = () -> {
            List<WebhookSubscription> subscriptions = webhooksRetriever.getSubscriptionsOfType(WebhookType.OrderShipped);
            log.info("Received OrderStatusChangedToShippedIntegrationEvent and got {} subscriptions to process", subscriptions.size());
            var webhook = new WebhookData(WebhookType.OrderShipped, event);
            webhooksSender.sendAll(subscriptions, webhook);
        };
        runnable.run();
        return runnable;
    }

}
