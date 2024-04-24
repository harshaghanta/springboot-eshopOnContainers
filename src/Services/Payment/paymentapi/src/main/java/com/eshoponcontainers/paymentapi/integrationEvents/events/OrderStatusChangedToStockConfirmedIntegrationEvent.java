package com.eshoponcontainers.paymentapi.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper=false) // Add this line
public class OrderStatusChangedToStockConfirmedIntegrationEvent extends IntegrationEvent {
    private int orderId;
}