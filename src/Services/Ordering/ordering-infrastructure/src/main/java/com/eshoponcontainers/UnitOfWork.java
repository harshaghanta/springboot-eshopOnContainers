package com.eshoponcontainers;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.seedWork.IUnitOfWork;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@Component
@Scope("prototype")
// @Slf4j
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
            entityManager.flush();
            
            List<Notification> domainEvents = DomainContext.getDomainEvents();
            if (domainEvents != null) {
                ListIterator<Notification> listIterator = domainEvents.listIterator();
                while (listIterator.hasNext()) {
                    Notification event = listIterator.next();
                    listIterator.remove();
                    pipeline.send(event);
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
