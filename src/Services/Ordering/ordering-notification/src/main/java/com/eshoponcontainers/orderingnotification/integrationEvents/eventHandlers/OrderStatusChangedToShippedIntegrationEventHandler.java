package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToShippedIntegrationEvent;

@Component
public class OrderStatusChangedToShippedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToShippedIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToShippedIntegrationEvent event) {
        
    }

}
