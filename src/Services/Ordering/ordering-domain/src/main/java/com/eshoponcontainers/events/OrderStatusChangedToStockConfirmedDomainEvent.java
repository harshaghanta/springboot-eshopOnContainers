package com.eshoponcontainers.events;

import an.awesome.pipelinr.Notification;
import lombok.Getter;

@Getter
public class OrderStatusChangedToStockConfirmedDomainEvent implements Notification {

    private int orderId;

    public OrderStatusChangedToStockConfirmedDomainEvent(Integer id) {
        this.orderId = id;
    }

}
