package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToCancelledIntegrationEvent;
import com.eshoponcontainers.orderingnotification.sse.NotificationMessage;
import com.eshoponcontainers.orderingnotification.sse.SseEmitterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedToCancelledIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToCancelledIntegrationEvent> {

    private final SseEmitterService sseEmitterService;

    @Override
    public void handle(OrderStatusChangedToCancelledIntegrationEvent event) {

        log.info("----- Handling integration event: {} at {} - ({})", event.getId(), "ordering-notification", event);

        NotificationMessage message = new NotificationMessage(event.getOrderId(), event.getOrderStatus());
        sseEmitterService.sendToUser(event.getBuyerName(), message);
    }

}
