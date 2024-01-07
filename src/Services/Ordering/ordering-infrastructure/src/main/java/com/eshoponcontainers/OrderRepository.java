package com.eshoponcontainers;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderRepository implements IOrderRepository, IUnitOfWork  {

    private final Pipeline pipeline;
    private final EntityManager entityManager;

    @Override
    public IUnitOfWork geUnitOfWork() {

        return this;
    }

    

    @Override
    public Order add(Order order) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public boolean update(Order order) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Order get(int orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public boolean save(Order order) {

        //Publish Domain Events
        Collection<Notification> domainEvents = order.getDomainEvents();

        domainEvents.stream().forEach(de -> pipeline.send(de));

        //Save Order Implementation


        return true;
    }
}
