package com.eshoponcontainers.events;

import java.time.LocalDate;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

import an.awesome.pipelinr.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStartedDomainEvent implements Notification {

    private Order order;
    private String userId;
    private String userName;
    private int cardTypeId;
    private String cardNumber;
    private String cardSecurityNumber;
    private String cardHolderName;
    private LocalDate cardExpiration; 
    

}
