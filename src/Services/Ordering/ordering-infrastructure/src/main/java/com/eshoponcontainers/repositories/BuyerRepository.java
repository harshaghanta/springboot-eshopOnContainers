package com.eshoponcontainers.repositories;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.config.EntityManagerUtil;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuyerRepository implements IBuyerRepository {    
    
    private final IUnitOfWork unitOfWork;

    @Override
    public IUnitOfWork getUnitOfWork() {
        return unitOfWork;
    }

    @Override
    public Buyer add(Buyer buyer) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in BuyerRepository Add", entityManager.hashCode());       
        entityManager.persist(buyer);
        return buyer;
    }

    @Override
    public Buyer update(Buyer buyer) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in BuyerRepository update", entityManager.hashCode());       
        entityManager.merge(buyer);
        //TODO: HACK : to be removed. Find out why merge is not triggering the preupdate event on the Buyer entity
        entityManager.flush();        
        return buyer;
    }

    @Override
    public Buyer find(String buyerIdentityUUID) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in BuyerRepository find", entityManager.hashCode());       
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Buyer> criteriaQuery = criteriaBuilder.createQuery(Buyer.class);
        Root<Buyer> root = criteriaQuery.from(Buyer.class);
        
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("identityUUID"), buyerIdentityUUID));
        
        Buyer buyer = entityManager.createQuery(criteriaQuery).getResultStream().findFirst().orElse(null);
        if(buyer == null)
            log.info("Buyer not found for id: {}", buyerIdentityUUID);
        return buyer;
    }

    @Override
    public Buyer findById(String id) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        log.info("EntityManager hashcode: {} in BuyerRepository find", entityManager.hashCode());       
        Buyer buyer = entityManager.find(Buyer.class, id);
        return buyer;
    }

}
