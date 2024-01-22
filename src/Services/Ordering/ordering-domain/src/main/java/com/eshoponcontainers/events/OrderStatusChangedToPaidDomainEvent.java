package com.eshoponcontainers.events;

import java.util.List;

import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderItem;

import an.awesome.pipelinr.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class OrderStatusChangedToPaidDomainEvent implements Notification {

    private Integer orderId;
    private List<OrderItem> orderItems;
}
