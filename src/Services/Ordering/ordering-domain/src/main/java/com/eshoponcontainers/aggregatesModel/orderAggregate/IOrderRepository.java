package com.eshoponcontainers.aggregatesModel.orderAggregate;

import com.eshoponcontainers.seedWork.IRepository;

public interface IOrderRepository extends IRepository<Order> {

    Order add(Order order);

    boolean update(Order order);

    Order get(int orderId);
}
