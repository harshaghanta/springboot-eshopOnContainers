package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private Collection<ConfirmedOrderStockItem> orderStockItems;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ConfirmedOrderStockItem {
        private int productId;
        private boolean hasStock;
    }
    
}
