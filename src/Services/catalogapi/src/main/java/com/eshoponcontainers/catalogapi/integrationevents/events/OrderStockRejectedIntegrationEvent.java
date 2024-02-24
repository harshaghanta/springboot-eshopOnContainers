package com.eshoponcontainers.catalogapi.integrationevents.events;

import java.util.Collection;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private Collection<ConfirmedOrderStockItem> orderStockItems;

}
