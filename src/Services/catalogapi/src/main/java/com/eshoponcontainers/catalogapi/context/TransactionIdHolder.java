package com.eshoponcontainers.catalogapi.context;

import java.util.UUID;

public class TransactionIdHolder {
    private static final ThreadLocal<UUID> currentTransactionId = new ThreadLocal<>();

    public static void setTransactionId(UUID transactionId) {
        currentTransactionId.set(transactionId);
    }

    public static UUID getTransactionId() {
        return currentTransactionId.get();
    }

    public static void clear() {
        currentTransactionId.remove();
    }
}