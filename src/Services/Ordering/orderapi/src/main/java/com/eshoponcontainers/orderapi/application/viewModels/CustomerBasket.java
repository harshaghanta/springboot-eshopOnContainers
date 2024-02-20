package com.eshoponcontainers.orderapi.application.viewModels;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBasket {

    private String buyerId;
    private Collection<BasketItem> items;
}
