package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToSubmittedIntegrationEvent;

public class OrderStatusChangedToSubmittedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToSubmittedIntegrationEvent> {

    @Override
    public Runnable handle(OrderStatusChangedToSubmittedIntegrationEvent event) {
        // TODO : HIGH : Need to notify the buyer of the updated state
        Runnable runnable = () -> {

        };
        
        return runnable;
    }

}
