package com.eshoponcontainers.catalogapi.integrationevents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ConfirmedOrderStockItem {

    private int productId;
    private boolean hasStock;

}
