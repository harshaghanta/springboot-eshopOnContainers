package com.eshoponcontainers.aggregatesModel.buyerAggregate;

import java.time.LocalDate;

import com.eshoponcontainers.exceptions.OrderingDomainException;
import com.eshoponcontainers.seedWork.Entity;

import lombok.Getter;

@Getter
public class PaymentMethod extends Entity {

    private String alias;
    private String cardNumber;
    private String securityNumber;
    private String cardHolderName;
    private LocalDate expiration;
    private int cardTypeId;
    private Buyer buyer;
    // private CardType cardType;

    protected PaymentMethod() {
        
    }

    public PaymentMethod(Buyer buyer, int cardTypeId, String alias, String cardNumber, String securityNumber, String cardHolderName, LocalDate expiration) {
    // public PaymentMethod(CardType cardType, String alias, String cardNumber, String securityNumber, String cardHolderName, LocalDate expiration) {
        
        if(cardNumber.isBlank() || cardNumber.isEmpty())
            throw new OrderingDomainException("cardNumber");
        
        if(securityNumber.isBlank() || securityNumber.isEmpty())
            throw new OrderingDomainException("securityNumber");

        if(cardHolderName.isBlank() || cardHolderName.isEmpty())
            throw new OrderingDomainException("cardHolderName");

        this.cardNumber = cardNumber;        
        this.securityNumber = securityNumber;
        this.cardHolderName = cardHolderName;

        if(expiration.isBefore(LocalDate.now()))
            throw new OrderingDomainException("expiration");

        this.alias = alias;
        this.expiration = expiration;
        this.cardTypeId = cardTypeId;
        this.buyer = buyer;
    }

    public boolean isEqualTo(int cardTypeId, String cardNumber, LocalDate expiration) {
        // return this.cardTypeId == cardTypeId 
        return this.cardTypeId == cardTypeId
        && this.cardNumber.equals(cardNumber) && this.expiration.equals(expiration);
    }

}
