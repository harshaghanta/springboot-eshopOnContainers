package com.eshoponcontainers.basketapi.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class BasketCheckout {
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipcode;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate cardExpiration;
    private String cardSecurityNumber;
    private int cardTypeId;
    private String buyer;
    private UUID requestId;
}
