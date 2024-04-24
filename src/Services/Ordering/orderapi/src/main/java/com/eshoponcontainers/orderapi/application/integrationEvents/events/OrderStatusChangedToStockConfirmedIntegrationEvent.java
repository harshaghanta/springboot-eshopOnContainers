package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class OrderStatusChangedToStockConfirmedIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
}
