package com.eshoponcontainers.webhooksapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProductPriceChangedIntegrationEvent extends IntegrationEvent {

    private int productId;
    private double oldPrice;
    private double newPrice;
}
