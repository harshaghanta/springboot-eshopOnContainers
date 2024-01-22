package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.events.OrderStatusChangedToAwaitingValidationDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockItem;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderStatusChangedToAwaitingValidationDomainEventHandler implements Notification.Handler<OrderStatusChangedToAwaitingValidationDomainEvent> {

    private final IOrderRepository orderRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public void handle(OrderStatusChangedToAwaitingValidationDomainEvent event) {
        
        Order order = orderRepository.get(event.getOrderId());
        List<OrderStockItem> orderItems = event.getOrderItems().stream()
            .map(item ->  new OrderStockItem(item.getProductId(), item.getUnits()))
            .collect(Collectors.toList());

        //TODO: HIGH: Need to Find TransactionID
        UUID transactionId = UUID.randomUUID();
        String buyerName = order.getBuyer().getName();
        var integrationEvent = new OrderStatusChangedToAwaitingValidationIntegrationEvent(event.getOrderId(), order.getOrderStatus().name(), buyerName, orderItems);
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent, transactionId);
    }

}
