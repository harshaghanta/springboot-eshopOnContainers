package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.BasketProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.orderingnotification.sse.NotificationMessage;
import com.eshoponcontainers.orderingnotification.sse.SseEmitterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BasketProductPriceChangedIntegrationEventHandler
        implements IntegrationEventHandler<BasketProductPriceChangedIntegrationEvent> {

    private final SseEmitterService sseEmitterService;

    @Override
    public void handle(BasketProductPriceChangedIntegrationEvent event) {
        log.info("----- Handling integration event: {} at {} - ({})", event.getId(), "ordering-notification", event);

        NotificationMessage message = NotificationMessage.basketPriceChange(
                event.getProductId(),
                event.getOldPrice(),
                event.getNewPrice());
        sseEmitterService.sendToUser(event.getUsername(), message);
    }
}