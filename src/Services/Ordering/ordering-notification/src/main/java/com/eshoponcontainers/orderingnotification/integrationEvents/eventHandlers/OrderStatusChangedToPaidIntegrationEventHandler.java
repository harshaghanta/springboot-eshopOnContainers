package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToPaidIntegrationEvent;

public class OrderStatusChangedToPaidIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    @Override
    public Runnable handle(OrderStatusChangedToPaidIntegrationEvent event) {
        // TODO : HIGH : Need to notify the buyer of the updated state
        Runnable runnable = () -> {

        };
        
        return runnable;
    }

}
