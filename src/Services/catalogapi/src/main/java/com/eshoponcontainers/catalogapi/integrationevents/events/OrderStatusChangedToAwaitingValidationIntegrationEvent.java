package com.eshoponcontainers.catalogapi.integrationevents.events;

import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusChangedToAwaitingValidationIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private Collection<OrderStockItem> orderStockItems;
    
}
