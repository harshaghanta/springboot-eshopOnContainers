package com.eshoponcontainers.orderapi.application.integrationEvents.events;


import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
    private Collection<OrderStockItem> orderStockItems;

    @AllArgsConstructor
    @Getter
    class OrderStockItem 
    {
        private int productId;
        private int units;
    }
}
