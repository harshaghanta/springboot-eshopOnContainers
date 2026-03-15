package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToCancelledIntegrationEvent;

@Component
public class OrderStatusChangedToCancelledIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToCancelledIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToCancelledIntegrationEvent event) {
        
    }

}
