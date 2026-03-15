package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;

public class OrderStatusChangedToStockConfirmedIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToStockConfirmedIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToStockConfirmedIntegrationEvent event) {
        
    }

}
