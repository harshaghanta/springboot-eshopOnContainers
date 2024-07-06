package com.eshoponcontainers.repositories;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.config.EntityManagerUtil;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderRepository implements IOrderRepository  {

    
    private final IUnitOfWork unitOfWork;

    @Override
    public IUnitOfWork getUnitOfWork() {

        return unitOfWork;
    }
    
    @Override
    public Order add(Order order) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in OrderRepository add", entityManager.hashCode());
        entityManager.persist(order);
        return order;
    }

    @Override
    public boolean update(Order order) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in OrderRepository update", entityManager.hashCode());
        order = entityManager.merge(order);
        return true;
    }

    @Override
    public Order get(int orderId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in OrderRepository get", entityManager.hashCode());
        return entityManager.find(Order.class, orderId);
    }
}
