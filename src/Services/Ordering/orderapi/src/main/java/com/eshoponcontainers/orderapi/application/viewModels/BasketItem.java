package com.eshoponcontainers.orderapi.application.viewModels;

public record BasketItem(String id, int productId, String productName, double unitPrice, double oldUnitPrice, int quantity, String pictureUrl) {

}
