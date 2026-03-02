package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.events.OrderStartedDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshoponcontainers.orderapi.services.TransactionContext;
import com.eshoponcontainers.services.impl.OutboxService;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor

public class ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler
        implements Notification.Handler<OrderStartedDomainEvent> {

    private final IBuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;
    private final Pipeline pipeline;
    private final OutboxService outboxService;

    @Override
    // @MyTransactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderStartedDomainEvent event) {
        TransactionContext.beginTransactionContext();
        var transactionId = TransactionContext.getTransactionId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.info("Transaction completed with COMMIT status.");
                    // orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
                    var domainEvents = DomainContext.getDomainEvents();
                    DomainContext.clearContext(); // Safety: clear after sending
                    if (domainEvents != null) {
                        domainEvents.forEach(event -> {
                            log.info("Publishing domain event: {}", event.getClass().getSimpleName());
                            pipeline.send(event);
                        });

                    }
                } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.error("Transaction completed with ROLLBACK status.");
                } else {
                    log.info("Transaction completed with unknown status: {}", status);
                }
                TransactionContext.clearContext();
            }
        });

        log.info("ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler invoked");

        var cardTypeId = event.getCardTypeId() != 0 ? event.getCardTypeId() : 1;
        Buyer buyer = buyerRepository.find(event.getUserId());
        boolean buyerExisted = !(buyer == null);

        if (!buyerExisted) {
            buyer = new Buyer(event.getUserId(), event.getUserName());
        }

        log.info("buyer: {}", buyer);

        String paymentMethodAlias = "Payment Method on " + new Date().toString();

        buyer.verifyOrAddPaymentMethod(cardTypeId, paymentMethodAlias, event.getCardNumber(),
                event.getCardSecurityNumber(), event.getCardHolderName(), event.getCardExpiration(),
                event.getOrder().getId());

        // Added to trigger force update on buyer as domain event is not getting raised
        // when there is no change
        buyer.setUpateTime(new Date());

        if (buyerExisted)
            buyerRepository.update(buyer);
        else
            buyerRepository.add(buyer);

        OrderStatusChangedToSubmittedIntegrationEvent integrationEvent = new OrderStatusChangedToSubmittedIntegrationEvent(
                event.getOrder().getId(), OrderStatus.Submitted.toString(), event.getUserId());
        // orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
        outboxService.saveToOutbox(integrationEvent);

        // buyerRepository.getUnitOfWork().saveChanges();
    }

}
