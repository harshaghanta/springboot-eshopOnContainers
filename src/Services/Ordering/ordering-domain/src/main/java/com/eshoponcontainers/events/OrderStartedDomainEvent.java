package com.eshoponcontainers.events;

import java.time.LocalDate;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

import an.awesome.pipelinr.Notification;

public class OrderStartedDomainEvent implements Notification {

    public OrderStartedDomainEvent(Order order, String userId, String userName, int cardTypeId, String cardNumber,
            String cardSecurityNumber, String cardHolderName, LocalDate cardExpiration) {
    }

}
