package com.eshoponcontainers.orderapi.services;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TransactionContext {
    // Initialize with an empty Deque to avoid null checks
    private static final ThreadLocal<Deque<UUID>> threadLocalStack = 
        ThreadLocal.withInitial(ArrayDeque::new);

    public static void beginTransactionContext() {
        UUID transactionId = UUID.randomUUID();
        log.info("Pushing new transaction context: {}", transactionId);
        threadLocalStack.get().push(transactionId);
    }

    public static UUID getTransactionId() {
        // Returns the top of the stack without removing it
        return threadLocalStack.get().peek();
    }

    public static void clearContext() {
        Deque<UUID> stack = threadLocalStack.get();
        if (!stack.isEmpty()) {
            UUID removedId = stack.pop();
            log.info("Popped transaction context: {}. Remaining in stack: {}", 
                removedId, stack.size());
        }
        
        // Fully cleanup the ThreadLocal if the stack is empty to prevent memory leaks
        if (stack.isEmpty()) {
            threadLocalStack.remove();
        }
    }
}
