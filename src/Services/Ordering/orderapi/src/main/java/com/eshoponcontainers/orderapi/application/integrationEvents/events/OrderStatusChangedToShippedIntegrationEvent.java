package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusChangedToShippedIntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
}
