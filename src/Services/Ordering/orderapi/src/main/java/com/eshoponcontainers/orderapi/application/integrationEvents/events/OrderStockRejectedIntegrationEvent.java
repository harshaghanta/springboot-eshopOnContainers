package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStockRejectedIntegrationEvent {

    private int orderId;
    private Collection<ConfirmedOrderStockItem> orderStockItems;

    @AllArgsConstructor
    @Data
    class ConfirmedOrderStockItem {
        private int productId;
        private boolean hasStock;
    }
    
}
