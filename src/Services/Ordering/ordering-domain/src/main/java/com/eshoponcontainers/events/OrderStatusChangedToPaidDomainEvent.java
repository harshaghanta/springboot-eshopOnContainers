package com.eshoponcontainers.events;

import java.util.List;

import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderItem;

import an.awesome.pipelinr.Notification;

public class OrderStatusChangedToPaidDomainEvent implements Notification {

    private Integer orderId;
    private List<OrderItem> orderItems;

    public OrderStatusChangedToPaidDomainEvent(Integer id, List<OrderItem> orderItems) {
        this.orderId = id;
        this.orderItems = orderItems;

    }

}
