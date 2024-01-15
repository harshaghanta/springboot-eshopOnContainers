package com.eshoponcontainers.aggregatesModel.buyerAggregate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.eshoponcontainers.seedWork.Entity;
import com.eshoponcontainers.seedWork.IAggregateRoot;

import lombok.Getter;
@Getter
public class Buyer extends Entity implements IAggregateRoot {

    private String identityUUID;
    private String name;
    private List<PaymentMethod> paymentMethods;

    protected Buyer() {
        paymentMethods = new ArrayList<>();
    }

    public Buyer(String identity, String name) {        
        this.identityUUID = identity;
        this.name = name;
    }

    public PaymentMethod verifyOrAddPaymentMethod(int cardTypeId, String alias, String cardNumber, String securityNumber, String cardHolderName,
        LocalDate expiration, int orderId) {

            return null;
    }

}
