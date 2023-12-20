package com.eshoponcontainers.events;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

import an.awesome.pipelinr.Notification;
import lombok.Getter;

@Getter
public class OrderCancelledDomainEvent implements Notification {

    private Order order;

    public OrderCancelledDomainEvent(Order order) {
        this.order = order;
    }

}
