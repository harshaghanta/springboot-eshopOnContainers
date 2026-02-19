package com.eshoponcontainers.orderapi.aop;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Order(0)
@RequiredArgsConstructor
@Slf4j
public class MyTransactionalAspect {

    private final Pipeline pipeline;

    @Pointcut("@annotation(myTransactional)")
    public void myTransactionalMethod(MyTransactional myTransactional) {}

    @Around("myTransactionalMethod(myTransactional)")
    public Object aroundMyTransactional(ProceedingJoinPoint pjp, MyTransactional myTransactional) throws Throwable {
        boolean isNew = false;
        if (TransactionContext.getTransactionId() == null) {
            TransactionContext.beginTransactionContext();
            isNew = true;
        }
        // Register synchronization for transaction completion
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    log.info("Transaction has been committed.");
                        var domainEvents = DomainContext.getDomainEvents();
                        if (domainEvents != null) {
                            log.info("Domain events count: {}", domainEvents.size());
                            domainEvents.forEach(e -> pipeline.send(e));
                        } else {
                            log.info("No domain events found.");
                        }

                }
            });
        }
        try {
            return pjp.proceed();
        } finally {
            if (isNew) {
                TransactionContext.clearContext();
            }
        }
    }
}
