package com.eshoponcontainers.catalogapi.integrationevents.events;

import java.math.BigDecimal;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProductPriceChangedIntegrationEvent extends IntegrationEvent {

    private int productId;
    private BigDecimal newPrice;
    private BigDecimal oldPrice;
}
