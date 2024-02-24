package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
 
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OrderStatusChangedToAwaitingValidationIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
    private Collection<OrderStockItem> orderStockItems;
}
