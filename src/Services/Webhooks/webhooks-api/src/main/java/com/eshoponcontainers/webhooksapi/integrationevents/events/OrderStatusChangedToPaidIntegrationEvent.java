package com.eshoponcontainers.webhooksapi.integrationevents.events;

import java.util.List;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private int orderId;
    private List<OrderStockItem> orderStockItems;

    public record OrderStockItem(int productId, int units) {
    }
}
