package com.eshoponcontainers.orderapi.application.viewModels;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerBasket {

    private String buyerId;
    private Collection<BasketItem> items;
}
