package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.events.OrderStatusChangedToPaidDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockItem;
import com.eshoponcontainers.repositories.BuyerRepository;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusChangedToPaidDomainEventHandler
        implements Notification.Handler<OrderStatusChangedToPaidDomainEvent> {

    private final IOrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public void handle(OrderStatusChangedToPaidDomainEvent event) {

        log.info("Order with Id: {} has been successfully updated to status {} ({})", event.getOrderId(),
                OrderStatus.Paid.name(), OrderStatus.Paid.getId());

        Order order = orderRepository.get(event.getOrderId());
        var buyer = buyerRepository.findById(order.getBuyerId().toString());

        List<OrderStockItem> orderStockList = event.getOrderItems().stream()
                .map(item -> new OrderStockItem(item.getProductId(), item.getUnits()))
                .collect(Collectors.toList());

        var integrationEvent = new OrderStatusChangedToPaidIntegrationEvent(event.getOrderId(),
                order.getOrderStatus().name(), buyer.getName(), orderStockList);
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);

    }

}
