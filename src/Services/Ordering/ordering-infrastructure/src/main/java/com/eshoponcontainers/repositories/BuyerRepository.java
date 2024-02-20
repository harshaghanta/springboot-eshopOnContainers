package com.eshoponcontainers.repositories;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuyerRepository implements IBuyerRepository {

    private final Pipeline pipeline;
    private final EntityManager entityManager;
    private final IUnitOfWork unitOfWork;

    @Override
    public IUnitOfWork getUnitOfWork() {
        return unitOfWork;
    }

    @Override
    public Buyer add(Buyer buyer) {       
        entityManager.persist(buyer);
        return buyer;
    }

    @Override
    public Buyer update(Buyer buyer) {
        entityManager.merge(buyer);
        return buyer;
    }

    @Override
    public Buyer find(String buyerIdentityUUID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Buyer> criteriaQuery = criteriaBuilder.createQuery(Buyer.class);
        Root<Buyer> root = criteriaQuery.from(Buyer.class);
        
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("identityUUID"), buyerIdentityUUID));
        
        Buyer buyer = entityManager.createQuery(criteriaQuery).getSingleResult();
        return buyer;
    }

    @Override
    public Buyer findById(String id) {
        Buyer buyer = entityManager.find(Buyer.class, id);
        return buyer;
    }

}
