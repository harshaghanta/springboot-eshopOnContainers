package com.eshoponcontainers.events;

import java.util.List;

import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderItem;

import an.awesome.pipelinr.Notification;
import lombok.Getter;

@Getter
public class OrderStatusChangedToAwaitingValidationDomainEvent implements Notification {

    private int orderId;
    private List<OrderItem> orderItems;

    public OrderStatusChangedToAwaitingValidationDomainEvent(Integer id, List<OrderItem> orderItems) {
        this.orderId = id;
        this.orderItems = orderItems;
    }

}
