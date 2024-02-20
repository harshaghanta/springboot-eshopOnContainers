package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.application.commands.SetStockRejectedOrderStatusCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SetStockRejectedOrderStatusCommandHandler
        implements Command.Handler<SetStockRejectedOrderStatusCommand, Boolean> {

    private final IOrderRepository orderRepository;

    @Override
    public Boolean handle(SetStockRejectedOrderStatusCommand command) {
        // Simulate a work time for validating the payment
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Order order = orderRepository.get(command.getOrderNumber());
        if (order == null)
            return false;

        order.SetCancelledStatusWhenStockIsRejected(command.getOrderStockItems());
        return orderRepository.getUnitOfWork().saveChanges();
    }

}
