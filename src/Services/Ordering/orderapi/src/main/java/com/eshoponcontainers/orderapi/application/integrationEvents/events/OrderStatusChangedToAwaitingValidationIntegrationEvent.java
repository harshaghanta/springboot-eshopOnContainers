package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Collection;
 
@AllArgsConstructor
@Getter
public class OrderStatusChangedToAwaitingValidationIntegrationEvent {

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
