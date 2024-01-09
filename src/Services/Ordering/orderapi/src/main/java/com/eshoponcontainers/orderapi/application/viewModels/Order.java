package com.eshoponcontainers.orderapi.application.viewModels;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

    private Integer orderNumber;
    private Instant date;
    private String status;
    private String description;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String state;
    private List<OrderItem> orderItems = new ArrayList<>();
    private Double total;
}
