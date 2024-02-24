package com.eshoponcontainers.catalogapi.integrationevents.events;

import java.util.Collection;
import java.util.List;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private Collection<OrderStatusChangedToPaidIntegrationEvent.OrderStockItem> items;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Getter
    @AllArgsConstructor
    public class OrderStockItem {
        
        private int productId;
        private int units;
    }
    
}
