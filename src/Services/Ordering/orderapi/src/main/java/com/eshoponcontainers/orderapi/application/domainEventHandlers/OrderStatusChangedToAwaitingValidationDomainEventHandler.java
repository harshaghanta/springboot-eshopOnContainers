package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.events.OrderStatusChangedToAwaitingValidationDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockItem;
import com.eshoponcontainers.repositories.BuyerRepository;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedToAwaitingValidationDomainEventHandler implements Notification.Handler<OrderStatusChangedToAwaitingValidationDomainEvent> {

    private final IOrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public void handle(OrderStatusChangedToAwaitingValidationDomainEvent event) {
        log.trace("Order with Id:{} has been successfully updated to status {}", event.getOrderId(), OrderStatus.AwaitingValidation.name());
        
        Order order = orderRepository.get(event.getOrderId());
        var buyer = buyerRepository.findById(order.getBuyerId().toString());

        List<OrderStockItem> orderItems = event.getOrderItems().stream()
            .map(item ->  new OrderStockItem(item.getProductId(), item.getUnits()))
            .collect(Collectors.toList());

        //TODO: HIGH: Need to Find TransactionID
        UUID transactionId = UUID.randomUUID();        
        var integrationEvent = new OrderStatusChangedToAwaitingValidationIntegrationEvent(event.getOrderId(), order.getOrderStatus().name(), buyer.getName(), orderItems);
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent, transactionId);
    }

}
