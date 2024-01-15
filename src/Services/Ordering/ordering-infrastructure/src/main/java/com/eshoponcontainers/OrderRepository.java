package com.eshoponcontainers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderRepository implements IOrderRepository  {

    private final Pipeline pipeline;
    private final EntityManager entityManager;
    private final IUnitOfWork unitOfWork;

    @Override
    public IUnitOfWork geUnitOfWork() {

        return unitOfWork;
    }

    

    @Override
    public Order add(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public boolean update(Order order) {
        entityManager.persist(order);
        return true;
    }

    @Override
    public Order get(int orderId) {
        return entityManager.find(Order.class, orderId);
    }
}
