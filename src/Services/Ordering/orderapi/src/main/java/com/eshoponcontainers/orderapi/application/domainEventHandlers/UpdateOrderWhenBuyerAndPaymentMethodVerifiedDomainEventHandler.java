package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler
        implements Notification.Handler<BuyerAndPaymentMethodVerifiedDomainEvent> {

    private final IOrderRepository orderRepository;
    private final Pipeline pipeline;

    @Override
    // @MyTransactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(BuyerAndPaymentMethodVerifiedDomainEvent event) {
        log.info("Receieved the BuyerAndPaymentMethodVerifiedDomainEvent with buyer: {} for order: {}",
                event.getBuyer().getId(),
                event.getOrderId());

        TransactionContext.beginTransactionContext();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.info("Order with Id: {} has been successfully updated with a payment method {} {}",
                            event.getOrderId(), event.getPaymentMethod().getId());
                    log.info("Transaction completed with COMMIT status.");
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
        var order = orderRepository.get(event.getOrderId());
        order.setBuyerId(event.getBuyer().getId());
        order.setPaymentMethodId(event.getPaymentMethod().getId());
        // orderRepository.getUnitOfWork().saveChanges();

    }

}
