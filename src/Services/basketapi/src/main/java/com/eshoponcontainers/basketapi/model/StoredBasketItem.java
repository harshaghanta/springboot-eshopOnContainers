package com.eshoponcontainers.basketapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredBasketItem {

    private String id;
    private int productId;
    private int quantity;
}