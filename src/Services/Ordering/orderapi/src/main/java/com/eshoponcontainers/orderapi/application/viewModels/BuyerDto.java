package com.eshoponcontainers.orderapi.application.viewModels;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BuyerDto {
    private String userId;
    private String name;
    private int cardTypeId;
    private String cardNumber;
    private String securityNumber;
    private String cardHolderName;
    private LocalDate expiration;
}
