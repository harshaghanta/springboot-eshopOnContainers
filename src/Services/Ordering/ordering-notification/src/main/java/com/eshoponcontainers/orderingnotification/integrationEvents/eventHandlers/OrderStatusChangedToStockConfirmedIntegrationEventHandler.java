package com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;

public class OrderStatusChangedToStockConfirmedIntegrationEventHandler implements IntegrationEventHandler<OrderStatusChangedToStockConfirmedIntegrationEvent> {

    @Override
    public Runnable handle(OrderStatusChangedToStockConfirmedIntegrationEvent event) {
        // TODO : HIGH : Need to notify the buyer of the updated state
        Runnable runnable = () -> {

        };
        runnable.run();
        return runnable;
    }

}
