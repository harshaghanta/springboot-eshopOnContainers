package com.eshoponcontainers.orderapi.application.commandHandlers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Address;
import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStartedIntegrationEvent;
import com.eshoponcontainers.orderapi.application.viewModels.OrderItemDTO;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements Command.Handler<CreateOrderCommand, Boolean> {

    private final OrderingIntegrationEventService orderingIntegrationEventService;
    private final IOrderRepository orderRepository;

    @Override
    public Boolean handle(CreateOrderCommand command) {
        var orderStartedIntegrationEvent = new OrderStartedIntegrationEvent(command.getUserId());
        UUID transactionId = UUID.randomUUID();
        orderingIntegrationEventService.addAndSaveEvent(orderStartedIntegrationEvent, transactionId);

        var address = new Address(command.getStreet(), command.getCity(), command.getState(), command.getCountry(),
                command.getZipCode());
        var order = new Order(command.getUserId(), command.getUserName(), address, command.getCardTypeId(),
                command.getCardNumber(), command.getCardSecurityNumber(), command.getCardHolderName(),
                command.getCardExpiration(), null, null);

        for (OrderItemDTO orderItem : command.getOrderItems()) {
            order.addOrderItem(orderItem.productId(), orderItem.productName(), orderItem.unitPrice()   , orderItem.discount(), orderItem.pictureUrl(), orderItem.units());
        }

        log.info("Creating Order: -", order);
        orderRepository.add(order);
        orderRepository.geUnitOfWork().saveChanges();
        return false;
    }

}
