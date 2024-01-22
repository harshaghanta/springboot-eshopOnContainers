package com.eshoponcontainers.events;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.PaymentMethod;

import an.awesome.pipelinr.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuyerAndPaymentMethodVerifiedDomainEvent implements Notification {

    private Buyer buyer;
    private PaymentMethod paymentMethod;
    private int orderId;
}
