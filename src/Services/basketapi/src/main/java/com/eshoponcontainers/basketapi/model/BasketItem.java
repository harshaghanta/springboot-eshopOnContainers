package com.eshoponcontainers.basketapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {

    private String id;
    private int productId;
    private String productName;
    private double unitPrice;
    private double oldUnitPrice;
    private int quantity;
    private String pictureUrl;   

}
