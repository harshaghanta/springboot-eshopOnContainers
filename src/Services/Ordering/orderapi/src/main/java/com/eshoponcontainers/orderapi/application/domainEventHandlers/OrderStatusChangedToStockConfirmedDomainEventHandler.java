package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.events.OrderStatusChangedToStockConfirmedDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import com.eshoponcontainers.repositories.BuyerRepository;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusChangedToStockConfirmedDomainEventHandler implements Notification.Handler<OrderStatusChangedToStockConfirmedDomainEvent> {

    private final IOrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public void handle(OrderStatusChangedToStockConfirmedDomainEvent event) {
        log.info("Order with Id: {} has been successfully updated to status {} ({})", event.getOrderId(), OrderStatus.StockConfirmed.name(), OrderStatus.StockConfirmed.getId());
        Order order = orderRepository.get(event.getOrderId());
        var buyer = buyerRepository.findById(order.getBuyerId().toString());

        log.info("Received OrderID: {} from order object and OrderID: {} from event object", order.getId(), event.getOrderId());
        // UUID transactionId = UUID.randomUUID();
        OrderStatusChangedToStockConfirmedIntegrationEvent integrationEvent = new OrderStatusChangedToStockConfirmedIntegrationEvent(order.getId(), order.getOrderStatus().name(), buyer.getName());
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
    }
}
