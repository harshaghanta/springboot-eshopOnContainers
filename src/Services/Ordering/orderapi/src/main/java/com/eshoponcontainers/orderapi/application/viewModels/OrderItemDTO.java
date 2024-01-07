package com.eshoponcontainers.orderapi.application.viewModels;

public record OrderItemDTO(int productId, String productName, double unitPrice, double discount, int units, String pictureUrl) {

}
