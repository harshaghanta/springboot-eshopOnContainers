package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToShippedIntegrationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedToShippedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToShippedIntegrationEvent> {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void handle(OrderStatusChangedToShippedIntegrationEvent event) {

        log.info("----- Handling integration event: {} at {} - ({})", event.getId(), "ordering-notification", event);

        // Create the payload expected by the Angular NotificationService
        NotificationMessage message = new NotificationMessage(event.getOrderId(), event.getOrderStatus());

        // Send to the specific user's queue
        // The destination matches the client's subscription: /user/queue/notifications
        messagingTemplate.convertAndSendToUser(
                event.getBuyerName(), // This must match the 'sub' or name in the JWT
                "/queue/notifications",
                message);
    }

}