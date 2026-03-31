package com.eshoponcontainers.orderapi.application.viewModels;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderItem(@JsonProperty("productname") String productName, int units, @JsonProperty("unitprice") BigDecimal unitPrice, String pictureUrl) {

}
