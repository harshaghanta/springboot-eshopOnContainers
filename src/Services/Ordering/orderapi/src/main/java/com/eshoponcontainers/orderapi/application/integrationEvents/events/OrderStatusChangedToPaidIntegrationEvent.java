package com.eshoponcontainers.orderapi.application.integrationEvents.events;


import java.util.List;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
    private List<OrderStockItem> orderStockItems;    
}
