package com.eshoponcontainers.orderapi.application.behaviors;

import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import an.awesome.pipelinr.Command;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(200)
@RequiredArgsConstructor
public class TransactionBehavior implements Command.Middleware {

    private final EntityManager entityManager;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        var className = command.getClass().getSimpleName();
        log.info("EntityManager hashcode: {} in TransactionBehavior", entityManager.hashCode());
        EntityTransaction transaction = entityManager.getTransaction();
        R response = null;
        UUID transactionId = null;
        try {
            if (transaction.isActive()) {
                log.info( "Already inside a transaction with id: {} ", TransactionContext.getTransactionId());
                return next.invoke();
            }

            transaction.begin();
            TransactionContext.beginTransactionContext();
            transactionId = TransactionContext.getTransactionId();
            log.info("----- Begin transaction {} for {} ({})", transactionId, className, command);
            response = next.invoke();
            transaction.commit();
            TransactionContext.clearContext();
            log.info("----- commit transaction {} for {} ",transactionId, className);
            orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
        } catch (Exception e) {
            log.error("ERROR Handling transaction for {}", className);
            throw e;
        }
        return response;
    }

    // @Override
    // public <N extends Notification> void invoke(N notification, Next next) {
    // var className = notification.getClass().getSimpleName();
    // EntityTransaction transaction = entityManager.getTransaction();

    // try {
    // if(transaction.isActive()) {
    // next.invoke();
    // return;
    // }

    // transaction.begin();
    // log.info("----- Begin transaction for {} ({})", className, notification);
    // next.invoke();
    // transaction.commit();
    // log.info("----- commit transaction for {} ", className);
    // orderingIntegrationEventService.publishEventsThroughEventBus(null);
    // } catch (Exception e) {
    // log.error("ERROR Handling transaction for {} ({})", transaction,
    // notification);
    // throw e;
    // }
    // }

}
