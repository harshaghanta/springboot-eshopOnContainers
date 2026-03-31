package com.eshoponcontainers.orderapi.application.viewModels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

    @JsonProperty("ordernumber")
    private Integer orderNumber;
    private LocalDate date;
    private String status;
    private String description;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String state;

    @JsonProperty("orderitems")
    private List<OrderItem> orderItems = new ArrayList<>();
    private Double total;
}
