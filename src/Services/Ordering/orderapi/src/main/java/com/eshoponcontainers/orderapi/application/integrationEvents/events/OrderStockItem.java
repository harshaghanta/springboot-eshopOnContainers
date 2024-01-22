package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStockItem {

    private int productId;
    private int units;
}
