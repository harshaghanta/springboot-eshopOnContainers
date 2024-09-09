package com.eshoponcontainers;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.config.EntityManagerUtil;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Component
// @Scope("prototype")
 @Slf4j
public class UnitOfWork implements IUnitOfWork {

    // @PersistenceContext
    
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Pipeline pipeline;

    @Override
    // @Transactional
    public boolean saveChanges() {
        // EntityTransaction transaction = entityManager.getTransaction();

        try {
            //Transaction is needed. with out it, the newly created entity id's are not available
            // if (!transaction.isActive()) {
            //     transaction.begin();
            // }

            // Flush the changes to synchronize with the database
            // This is required for Preupdate event to be triggered.

            
            // log.info("Before flush call on Entity manager from thread: {}", Thread.currentThread().getName());
            //NOTE: Needed as there are places where in persist or update are not called 
            // in the entitymanager. and this flush will trigger the domainevent collection
            // which is needed for triggering domainevents 
            entityManager.flush();
            // EntityManagerUtil.getEntityManager().flush();
            //With out flush call BuyerAndPaymentMethodVerifiedDomainEvent is not captured
            //Added Cascade Persist and remove for Buyer
            // Thread.sleep(5_000);
            // log.info("After flush call on Entity manager from thread: {}", Thread.currentThread().getName());

            Set<Notification> domainEvents = DomainContext.getDomainEvents();
            if (domainEvents != null) {
                Iterator<Notification> listIterator = domainEvents.iterator();
                while (listIterator.hasNext()) {
                    Notification event = listIterator.next();
                    listIterator.remove();
                    pipeline.send(event);
                   
                    // Executors.newCachedThreadPool().submit(() -> {
                    //     DomainContext.clearContext();
                    //     pipeline.send(event);
                    // });                    
                    
                }
                
                // if(domainEvents.size() > 0) {
                //     var firstEvent = domainEvents.get(0);
                //     domainEvents.clear();
                //     DomainContext.clearContext();
                //     pipeline.send(firstEvent);
                // }
                    
            }
            // if(transaction.isActive())
            //     transaction.commit();

            // DomainContext.clearContext();
            return true;
        } catch (Exception e) {
            // Handle exceptions, log, and possibly roll back the transaction
            // if (transaction.isActive()) {
            //     transaction.rollback();
            // }
            e.printStackTrace(); // log the exception
            return false;
        }
    }

}
