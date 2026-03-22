package com.eshoponcontainers.orderingnotification.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderStatusChangedToStockConfirmedIntegrationEvent extends IntegrationEvent {
    private int orderId;
    private String orderStatus;
    private String buyerName;
}
