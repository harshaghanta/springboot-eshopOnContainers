package com.eshoponcontainers.webhooksapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangedToShippedIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private String orderStatus;
    private String buyerName;
}
