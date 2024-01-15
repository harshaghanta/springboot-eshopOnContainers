package com.eshoponcontainers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


@Component
public class UnitOfWork implements IUnitOfWork {

    // @PersistenceContext
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private Pipeline pipeline;

    @Override
    // @Transactional
        public boolean saveChanges() {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }            

            // Flush the changes to synchronize with the database       

            entityManager.flush();
            List<Notification> domainEvents = DomainContext.getDomainEvents();
            if(domainEvents != null)
                domainEvents.stream().forEach(de -> pipeline.send(de));
            DomainContext.clearContext();

            transaction.commit();
            return true;
        } catch (Exception e) {
            // Handle exceptions, log, and possibly roll back the transaction
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // log the exception
            return false;
        }
    }



}
