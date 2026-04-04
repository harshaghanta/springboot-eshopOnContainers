package com.eshoponcontainers.basketapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketPriceChangedNotification {

    private String buyerId;
    private String username;
    private int productId;
    private double previousPrice;
    private double currentPrice;
    private double lastNotifiedPrice;
    private long notifiedAtEpochMillis;
}