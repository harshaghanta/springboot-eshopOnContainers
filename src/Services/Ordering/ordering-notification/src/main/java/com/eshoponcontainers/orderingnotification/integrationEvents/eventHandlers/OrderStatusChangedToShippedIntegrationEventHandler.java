package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToShippedIntegrationEvent;

@Component
public class OrderStatusChangedToShippedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToShippedIntegrationEvent> {

    @Override
    public Runnable handle(OrderStatusChangedToShippedIntegrationEvent event) {
        // TODO : HIGH : Need to notify the buyer of the updated state
        Runnable runnable = () -> {

        };
        runnable.run();
        return runnable;
    }

}
