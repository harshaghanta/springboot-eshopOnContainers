package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.orderapi.application.viewModels.CustomerBasket;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {

    private String userId;
    private String username;
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipCode;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate cardExpiration;
    private String cardSecurityNumber;
    private int cardTypeId;
    private String buyer;
    private UUID requestId;
    private CustomerBasket basket;

}
