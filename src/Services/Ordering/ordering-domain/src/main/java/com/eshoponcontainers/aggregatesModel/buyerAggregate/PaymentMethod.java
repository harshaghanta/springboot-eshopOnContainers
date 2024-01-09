package com.eshoponcontainers.aggregatesModel.buyerAggregate;

import java.time.LocalDate;

import com.eshoponcontainers.seedWork.Entity;

public class PaymentMethod extends Entity {

    private String alias;
    private String cardNumber;
    private String securityNumber;
    private String cardHolderName;
    private LocalDate expiration;
    private int cardTypeId;

    protected PaymentMethod() {
        
    }

    public PaymentMethod(int cardTypeId, String alias, String cardNumber, String securityNumber, String cardHolderName, LocalDate expiration) {
        this.cardNumber = cardNumber;
        this.cardTypeId = cardTypeId;
        this.alias = alias;
        this.securityNumber = securityNumber;
        this.expiration = expiration;
        this.cardHolderName = cardHolderName;
    }

    public boolean isEqualTo(int cardTypeId, String cardNumber, LocalDate expiration) {
        return this.cardTypeId == cardTypeId && this.cardNumber.equals(cardNumber) && this.expiration.equals(expiration);
    }

}
