package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.events.OrderStatusChangedToAwaitingValidationDomainEvent;
import com.eshoponcontainers.orderapi.aop.MyTransactional;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockItem;
import com.eshoponcontainers.orderapi.services.TransactionContext;
import com.eshoponcontainers.repositories.BuyerRepository;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedToAwaitingValidationDomainEventHandler
        implements Notification.Handler<OrderStatusChangedToAwaitingValidationDomainEvent> {

    private final IOrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderStatusChangedToAwaitingValidationDomainEvent event) {
        log.info("Order with Id:{} has been successfully updated to status {}", event.getOrderId(),
                OrderStatus.AwaitingValidation.name());

        TransactionContext.beginTransactionContext();
        var transactionId = TransactionContext.getTransactionId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.info("Transaction completed with COMMIT status.");
                    orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
               
                } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.error("Transaction completed with ROLLBACK status.");
                } else {
                    log.info("Transaction completed with unknown status: {}", status);
                }
                TransactionContext.clearContext();
            }
        });

        Order order = orderRepository.get(event.getOrderId());
        var buyer = buyerRepository.findById(order.getBuyerId().toString());

        List<OrderStockItem> orderItems = event.getOrderItems().stream()
                .map(item -> new OrderStockItem(item.getProductId(), item.getUnits()))
                .collect(Collectors.toList());

        var integrationEvent = new OrderStatusChangedToAwaitingValidationIntegrationEvent(event.getOrderId(),
                order.getOrderStatus().name(), buyer.getName(), orderItems);
        // log.info("Saving OrderStatusChangedToAwaitingValidationIntegrationEvent with transactionId:{}", TransactionContext.getTransactionId());
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
    }

}
