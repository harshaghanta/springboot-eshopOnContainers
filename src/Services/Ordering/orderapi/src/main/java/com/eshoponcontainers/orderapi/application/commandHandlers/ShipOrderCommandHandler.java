package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.aop.MyTransactional;
import com.eshoponcontainers.orderapi.application.commands.ShipOrderCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShipOrderCommandHandler implements Command.Handler<ShipOrderCommand, Boolean> {

    private final IOrderRepository orderRepository;

    @Override
    @MyTransactional
    public Boolean handle(ShipOrderCommand command) {
        Order order = orderRepository.get(command.getOrderNumber());
        if (order == null)
            return false;

        order.SetShippedStatus();
        // return orderRepository.getUnitOfWork().saveChanges();
        return true;

    }

}
