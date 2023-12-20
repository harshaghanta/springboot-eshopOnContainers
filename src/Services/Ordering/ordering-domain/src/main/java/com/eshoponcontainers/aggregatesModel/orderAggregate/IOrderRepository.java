package com.eshoponcontainers.aggregatesModel.orderAggregate;

public interface IOrderRepository {

    Order add(Order order);

    void update(Order order);

    Order get(int orderId);
}
