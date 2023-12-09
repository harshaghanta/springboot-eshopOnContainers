package com.eshoponcontainers.basketapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProductPriceChangedIntegrationEvent extends IntegrationEvent {

    private int productId;
    private double newPrice;
    private double oldPrice;
}
