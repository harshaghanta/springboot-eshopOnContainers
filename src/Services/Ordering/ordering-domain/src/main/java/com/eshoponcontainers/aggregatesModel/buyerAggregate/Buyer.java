package com.eshoponcontainers.aggregatesModel.buyerAggregate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eshoponcontainers.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import com.eshoponcontainers.seedWork.Entity;
import com.eshoponcontainers.seedWork.IAggregateRoot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Buyer extends Entity implements IAggregateRoot {

    private String identityUUID;
    private String name;
    private List<PaymentMethod> paymentMethods;
    private Date updateTime;

    protected Buyer() {
        paymentMethods = new ArrayList<>();
        this.updateTime = new Date();
    }

    public Buyer(String identity, String name) {
        this();
        this.identityUUID = identity;
        this.name = name;
    }    

    public void setUpateTime(Date upateTime) {
        this.updateTime = upateTime;
    }

    public PaymentMethod verifyOrAddPaymentMethod(int cardTypeId, String alias, String cardNumber,
            String securityNumber, String cardHolderName,
            LocalDate expiration, int orderId) {

        var existingPayment = paymentMethods.stream().filter(meth -> meth.isEqualTo(cardTypeId, cardNumber, expiration))
                .findFirst();

        boolean isPresent = existingPayment.isPresent();
        if(!isPresent)
            log.info("Payment method not exist for buyer {}.", this.identityUUID);

        if (isPresent) {
            addDomainEvent(new BuyerAndPaymentMethodVerifiedDomainEvent(this, existingPayment.get(), orderId));
            return existingPayment.get();
        }

        log.info("Adding payment method to the InMemory collection");
        var payment = new PaymentMethod(this, cardTypeId, alias, cardNumber, securityNumber, cardHolderName, expiration);
        paymentMethods.add(payment);

        addDomainEvent(new BuyerAndPaymentMethodVerifiedDomainEvent(this, payment, orderId));

        return payment;
    }

}
