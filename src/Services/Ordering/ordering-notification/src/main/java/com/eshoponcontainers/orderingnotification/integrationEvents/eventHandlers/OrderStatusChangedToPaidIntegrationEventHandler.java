package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToPaidIntegrationEvent;

public class OrderStatusChangedToPaidIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToPaidIntegrationEvent event) {
        
    }

}
