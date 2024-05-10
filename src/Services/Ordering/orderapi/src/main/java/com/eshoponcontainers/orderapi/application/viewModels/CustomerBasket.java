package com.eshoponcontainers.orderapi.application.viewModels;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerBasket {

    private String buyerId;
    private Collection<BasketItem> items;
}
