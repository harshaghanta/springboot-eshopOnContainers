package com.eshoponcontainers.orderapi.services;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TransactionContext {

    private static ThreadLocal<UUID> threadLocal = new ThreadLocal<>();

    public static void beginTransactionContext() {
        UUID transactionId = UUID.randomUUID();
        log.info("Starting transaction context with transactionId:{}", transactionId);
        threadLocal.set(transactionId);
    }

    public static UUID getTransactionId() {
        return threadLocal.get();
    }

    public static void clearContext() {
        log.info("Ending transaction context for transactionId:{}", threadLocal.get());
        threadLocal.remove();
    }
}
