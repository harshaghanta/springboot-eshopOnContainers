package com.eshoponcontainers.basketapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BasketProductPriceChangedIntegrationEvent extends IntegrationEvent {

    private String buyerId;
    private String username;
    private int productId;
    private double oldPrice;
    private double newPrice;
}