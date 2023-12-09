package com.eshoponcontainers.basketapi.model;

import lombok.Data;

@Data
public class BasketItem {

    private String id;
    private int productId;
    private String productName;
    private double unitPrice;
    private double oldUnitPrice;
    private int quantity;
    private String pictureUrl;   

}
