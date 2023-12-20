package com.eshoponcontainers.events;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

import an.awesome.pipelinr.Notification;
import lombok.Getter;

@Getter
public class OrderShippedDomainEvent implements Notification {

    private Order order;

    public OrderShippedDomainEvent(Order order) {
        this.order = order;

    }

}
