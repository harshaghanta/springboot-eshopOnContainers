package com.eshoponcontainers.catalogapi.aspects;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.catalogapi.context.TransactionIdHolder;

@Aspect
@Component
public class TransactionAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void beforeTransaction(JoinPoint joinPoint) {
        TransactionIdHolder.setTransactionId(UUID.randomUUID());
        System.out.println("Transaction started with ID: " + TransactionIdHolder.getTransactionId().toString());
    }

    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void afterTransaction(JoinPoint joinPoint) {
        System.out.println("Transaction ended with ID: " + TransactionIdHolder.getTransactionId());
        TransactionIdHolder.clear();
    }
}