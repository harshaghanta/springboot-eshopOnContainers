package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.application.commands.CancelOrderCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CancelOrderCommandHandler implements Command.Handler<CancelOrderCommand, Boolean> {

    private final IOrderRepository orderRepository;

    @Override
    public Boolean handle(CancelOrderCommand command) {
        Order order = orderRepository.get(command.getOrderNumber());
        if(order == null)
            return false;
        
        order.SetCancelledStatus();
        orderRepository.geUnitOfWork().saveChanges();
        
        return true;
    }

}
