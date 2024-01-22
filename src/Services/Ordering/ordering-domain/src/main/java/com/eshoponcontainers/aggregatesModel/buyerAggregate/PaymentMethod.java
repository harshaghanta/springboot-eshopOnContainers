package com.eshoponcontainers.aggregatesModel.buyerAggregate;

import java.time.LocalDate;

import com.eshoponcontainers.seedWork.Entity;

import lombok.Getter;

@Getter
public class PaymentMethod extends Entity {

    private String alias;
    private String cardNumber;
    private String securityNumber;
    private String cardHolderName;
    private LocalDate expiration;
    // private int cardTypeId;    
    private CardType cardType;

    protected PaymentMethod() {
        
    }

    // public PaymentMethod(int cardTypeId, String alias, String cardNumber, String securityNumber, String cardHolderName, LocalDate expiration) {
    public PaymentMethod(CardType cardType, String alias, String cardNumber, String securityNumber, String cardHolderName, LocalDate expiration) {
        this.cardNumber = cardNumber;
        // this.cardTypeId = cardTypeId;
        this.cardType.getId() .equals(cardType.getId());
        this.alias = alias;
        this.securityNumber = securityNumber;
        this.expiration = expiration;
        this.cardHolderName = cardHolderName;
    }

    public boolean isEqualTo(int cardTypeId, String cardNumber, LocalDate expiration) {
        // return this.cardTypeId == cardTypeId 
        return this.cardType.getId().equals(cardType.getId())
        && this.cardNumber.equals(cardNumber) && this.expiration.equals(expiration);
    }

}
