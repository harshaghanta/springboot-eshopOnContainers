package com.eshoponcontainers.catalogapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStockConfirmedIntegrationEvent extends IntegrationEvent {

    private int orderId;
}
