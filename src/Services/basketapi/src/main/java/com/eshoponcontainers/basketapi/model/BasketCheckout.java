package com.eshoponcontainers.basketapi.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
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
