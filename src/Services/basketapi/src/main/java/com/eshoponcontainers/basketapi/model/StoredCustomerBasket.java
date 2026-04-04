package com.eshoponcontainers.basketapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StoredCustomerBasket {

    private String buyerId;
    private List<StoredBasketItem> items = new ArrayList<>();

    public StoredCustomerBasket() {
    }

    public StoredCustomerBasket(String buyerId) {
        this.buyerId = buyerId;
    }
}