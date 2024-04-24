package com.eshoponcontainers.catalogapi.integrationevents.events;

import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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
