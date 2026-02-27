package com.eshoponcontainers.orderapi.aop;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Order(100)
@RequiredArgsConstructor
@Slf4j
public class MyTransactionalAspect {

    private final Pipeline pipeline;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Pointcut("@annotation(myTransactional) && !within(com.sun.proxy..*) && !within(org.springframework.aop..*)")
    public void myTransactionalMethod(MyTransactional myTransactional) {

    }

    private void registerSynchronization(UUID transactionId) {

        log.info("Registering transaction synchronization for ID: {}", transactionId);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    // 1. Publish Integration Events NOW (The DB commit is final)
                    orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
                    
                    log.info("Transaction {} committed. Processing domain events.", transactionId);

                    var domainEvents = DomainContext.getDomainEvents();
                    DomainContext.clearContext();

                    if (domainEvents != null && !domainEvents.isEmpty()) {
                        domainEvents.forEach(event -> {
                            log.info("Publishing domain event: {}", event.getClass().getSimpleName());
                            pipeline.send(event);
                        });
                        // Clear context immediately after processing to prevent double-processing
                        
                    }
                } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.error("Transaction {} rolled back. Domain events discarded.", transactionId);
                    DomainContext.clearContext(); // Still clear to avoid memory leaks
                } else {
                    log.info("Transaction {} completed with status: {}", transactionId, status);
                }
            }
        });
    }

    @Around("myTransactionalMethod(myTransactional)")
    public Object aroundMyTransactional(ProceedingJoinPoint pjp, MyTransactional myTransactional) throws Throwable {
        log.info("Entered aroundMyTransactional for method: {}", pjp.getSignature().toShortString());

        // Check if we should force a new transaction context
        boolean forceNew = myTransactional.propagation() == Propagation.REQUIRES_NEW;
        UUID existingTransactionId = TransactionContext.getTransactionId();

        boolean isNew = false;

        UUID transactionId;

        // Logic: Create new if none exists OR if propagation is REQUIRES_NEW
        if (existingTransactionId == null || forceNew) {
            if (forceNew && existingTransactionId != null) {
                log.info("Suspending existing TransactionID: {} for REQUIRES_NEW", existingTransactionId);
            }

            TransactionContext.beginTransactionContext();
            transactionId = TransactionContext.getTransactionId();
            isNew = true;
        } else {
            transactionId = existingTransactionId;
            log.info("Joining existing TransactionContext with ID: {}", transactionId);
        }

        try {

            // 1. Let the method (and Spring's @Transactional) run first
            Object result = pjp.proceed();

            // 2. NOW the transaction is active. Register your hook here.
            var isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Is actual transaction active: {}", isActualTransactionActive);
            if (!isActualTransactionActive)
                log.warn("No actual transaction: Skipping TransactionSynchronizationManager registration");

            if (isActualTransactionActive) {
                registerSynchronization(transactionId);
            }

            return result;
        } catch (Exception ex) {
            log.error("Exception in aroundMyTransactional: {}", ex.getMessage(), ex);
            throw ex;
        } finally {
            if (isNew) {
                TransactionContext.clearContext();
                // orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);

                // If we suspended a transaction, you might need to restore it here
                // depending on how your TransactionContext is implemented internally.
            }
        }
    }
}
