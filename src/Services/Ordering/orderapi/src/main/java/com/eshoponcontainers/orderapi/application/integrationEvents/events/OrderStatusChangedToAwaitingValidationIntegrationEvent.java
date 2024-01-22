package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
 
@AllArgsConstructor
@Getter
public class OrderStatusChangedToAwaitingValidationIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
    private Collection<OrderStockItem> orderStockItems;
}
