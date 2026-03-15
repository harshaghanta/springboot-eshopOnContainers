package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;

@Component
public class OrderStatusChangedToAwaitingValidationIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToAwaitingValidationIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToAwaitingValidationIntegrationEvent event) {
        
    }

}
