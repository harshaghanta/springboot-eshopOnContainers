package com.eshoponcontainers.basketapi.integrationevents.events;

import java.time.LocalDate;
import java.util.UUID;

import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {

    private String userId;
    private String userName;
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

    public UserCheckoutAcceptedIntegrationEvent(String userId, String userName, String city, String street,
        String state, String country, String zipCode, String cardNumber, String cardHolderName,
        LocalDate cardExpiration, String cardSecurityNumber, int cardTypeId, String buyer, UUID requestId,
        CustomerBasket basket)
    {
        this.userId = userId;
        this.userName = userName;
        this.city = city;
        this.street = street;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardExpiration = cardExpiration;
        this.cardSecurityNumber = cardSecurityNumber;
        this.cardTypeId = cardTypeId;
        this.buyer = buyer;
        this.basket = basket;
        this.requestId = requestId;
    }
}
