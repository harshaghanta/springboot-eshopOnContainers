package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.application.commands.SetAwaitingValidationOrderStatusCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SetAwaitingValidationOrderStatusCommandHandler implements Command.Handler<SetAwaitingValidationOrderStatusCommand, Boolean> {

    private final IOrderRepository orderRepository;
    @Override
    public Boolean handle(SetAwaitingValidationOrderStatusCommand command) {
        Order order = orderRepository.get(command.getOrderNumber());
        if(order == null)
            return false;
        
        order.setAwaitingValidationStatus();
        return orderRepository.geUnitOfWork().saveChanges();
    }

}
