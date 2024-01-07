package com.eshoponcontainers.orderapi.application.viewModels;

import java.math.BigDecimal;

public record OrderItem(String productName, int units, BigDecimal unitPrice, String pictureUrl) {

}
