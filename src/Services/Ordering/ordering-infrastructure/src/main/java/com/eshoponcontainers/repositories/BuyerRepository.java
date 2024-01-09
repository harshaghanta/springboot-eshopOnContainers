package com.eshoponcontainers.repositories;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuyerRepository implements IBuyerRepository {

    private final Pipeline pipeline;
    private final EntityManager entityManager;

    @Override
    public IUnitOfWork geUnitOfWork() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'geUnitOfWork'");
    }

    @Override
    public Buyer add(Buyer buyer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public Buyer update(Buyer buyer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Buyer find(String buyerIdentityUUID) {
        return null;
    }

    @Override
    public Buyer findById(String id) {
        Buyer buyer = entityManager.find(Buyer.class, id);
        return buyer;
    }

}
