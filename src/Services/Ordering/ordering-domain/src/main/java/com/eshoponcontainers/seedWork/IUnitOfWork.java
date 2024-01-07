package com.eshoponcontainers.seedWork;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

public interface IUnitOfWork {

    boolean save(Order order);
}
