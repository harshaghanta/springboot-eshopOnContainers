package com.eshoponcontainers.basketapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CustomerBasket {
    private String buyerId = null;
    private List<BasketItem> items = new ArrayList<BasketItem>();

    public CustomerBasket() {        
    }

    public CustomerBasket(String customerId) {
        buyerId = customerId;
    }
}
