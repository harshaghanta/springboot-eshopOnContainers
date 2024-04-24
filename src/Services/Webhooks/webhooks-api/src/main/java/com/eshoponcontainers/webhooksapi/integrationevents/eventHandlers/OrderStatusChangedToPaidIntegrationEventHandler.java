package com.eshoponcontainers.webhooksapi.integrationevents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.webhooksapi.entities.WebhookData;
import com.eshoponcontainers.webhooksapi.entities.WebhookType;
import com.eshoponcontainers.webhooksapi.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.webhooksapi.services.WebhooksRetriever;
import com.eshoponcontainers.webhooksapi.services.WebhooksSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusChangedToPaidIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    private final WebhooksRetriever retriever;
    private final WebhooksSender sender;
    @Override
    public Runnable handle(OrderStatusChangedToPaidIntegrationEvent event) {
        Runnable runnable = () -> {
            var subscriptions = retriever.getSubscriptionsOfType(WebhookType.OrderPaid);
            log.info("Received OrderStatusChangedToPaidIntegrationEvent and got {} subscriptions to process", subscriptions.size());

            var whook = new WebhookData(WebhookType.OrderPaid, event);
            log.info(null, "Sending WebhookData: {} to {} subscriptions", whook, subscriptions.size());
            sender.sendAll(subscriptions, whook);
        };
        runnable.run();
        return runnable;
    }
}
