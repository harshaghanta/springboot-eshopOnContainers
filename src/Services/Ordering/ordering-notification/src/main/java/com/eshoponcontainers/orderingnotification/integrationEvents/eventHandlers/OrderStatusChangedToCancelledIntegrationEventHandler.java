package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToCancelledIntegrationEvent;

@Component
public class OrderStatusChangedToCancelledIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToCancelledIntegrationEvent> {

    @Override
    public Runnable handle(OrderStatusChangedToCancelledIntegrationEvent event) {
        // TODO : HIGH : Need to notify the buyer of the updated state
        Runnable runnable = () -> {

        };
        
        return runnable;
    }

}
