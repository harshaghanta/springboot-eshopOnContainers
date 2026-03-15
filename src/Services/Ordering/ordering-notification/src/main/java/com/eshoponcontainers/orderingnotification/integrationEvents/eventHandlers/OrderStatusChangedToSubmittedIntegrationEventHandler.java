package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToSubmittedIntegrationEvent;

public class OrderStatusChangedToSubmittedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToSubmittedIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToSubmittedIntegrationEvent event) {
    
    }

}
