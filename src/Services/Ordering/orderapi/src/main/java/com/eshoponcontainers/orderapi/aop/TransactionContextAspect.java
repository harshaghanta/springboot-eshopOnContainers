package com.eshoponcontainers.orderapi.aop;

import com.eshoponcontainers.orderapi.services.TransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Order(0) // Ensure this runs before other aspects if needed
public class TransactionContextAspect {

    @Pointcut("@annotation(transactional)")
    public void transactionalMethod(Transactional transactional) {}

    @Around("transactionalMethod(transactional)")
    public Object aroundTransactional(ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
        boolean isNew = false;
        if (TransactionContext.getTransactionId() == null) {
            TransactionContext.beginTransactionContext();
            isNew = true;
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
