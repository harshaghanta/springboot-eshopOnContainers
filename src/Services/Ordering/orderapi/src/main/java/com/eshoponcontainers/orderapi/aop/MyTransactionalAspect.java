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

    @Pointcut("@annotation(myTransactional)")
    public void myTransactionalMethod(MyTransactional myTransactional) {
    }

    @Around("myTransactionalMethod(myTransactional)")
    public Object aroundMyTransactional(ProceedingJoinPoint pjp, MyTransactional myTransactional) throws Throwable {
        log.info("Entered aroundMyTransactional for method: {}", pjp.getSignature().toShortString());
        boolean isNew = false;

        UUID transactionId = TransactionContext.getTransactionId();

        if (transactionId == null) {
            TransactionContext.beginTransactionContext();
            transactionId = TransactionContext.getTransactionId();

            isNew = true;
        } else {
            log.info("TransactionContext already initialized with TransactionID: {}", transactionId);
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
                log.info("Registering transaction synchronization");

                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        log.info("Transaction committed successfully!");
                        var domainEvents = DomainContext.getDomainEvents();
                        if (domainEvents != null) {
                            domainEvents.forEach(event -> {
                                log.info("Publishing domain event: {}", event.getClass().getSimpleName());
                                pipeline.send(event);
                            });
                            DomainContext.clearContext(); // Safety: clear after sending
                        }
                    }
                });
            }

            return result;

        } catch (Exception ex) {
            log.error("Exception in aroundMyTransactional: {}", ex.getMessage(), ex);
            throw ex; // Rethrow to ensure transaction rollback
        } finally {
            // 4. Cleanup Context
            if (isNew) {
                // Integration events usually happen here
                TransactionContext.clearContext();
                orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
                
            }
        }
    }
}
