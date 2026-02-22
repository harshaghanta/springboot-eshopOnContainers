package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.aop.MyTransactional;
import com.eshoponcontainers.orderapi.application.commands.SetAwaitingValidationOrderStatusCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor

@Slf4j
public class SetAwaitingValidationOrderStatusCommandHandler implements Command.Handler<SetAwaitingValidationOrderStatusCommand, Boolean> {

    private final IOrderRepository orderRepository;
    @Override
    @MyTransactional
    public Boolean handle(SetAwaitingValidationOrderStatusCommand command) {
        log.info("Executing SetAwaitingValidationOrderStatusCommand & Fetching Order: {}", command.getOrderNumber());
        Order order = orderRepository.get(command.getOrderNumber());
        if(order == null) {
            log.warn("Order not found: {}", command.getOrderNumber());
            return false;
        }
        order.setAwaitingValidationStatus();
        log.info("Order status set to Awaiting Validation: {}", command.getOrderNumber());
        // return orderRepository.getUnitOfWork().saveChanges();
        return true;
    }
    

}
