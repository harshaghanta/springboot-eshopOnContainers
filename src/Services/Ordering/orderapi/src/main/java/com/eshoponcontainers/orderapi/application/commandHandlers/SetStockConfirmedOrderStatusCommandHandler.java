package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.aop.MyTransactional;
import com.eshoponcontainers.orderapi.application.commands.SetStockConfirmedOrderStatusCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class SetStockConfirmedOrderStatusCommandHandler
        implements Command.Handler<SetStockConfirmedOrderStatusCommand, Boolean> {

    private final IOrderRepository orderRepository;

    @Override
    @MyTransactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean handle(SetStockConfirmedOrderStatusCommand command) {
        // Simulate a work time for validating the payment
        // try {
        // Thread.sleep(10000);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        Order order = orderRepository.get(command.getOrderNumber());
        if (order == null)
            return false;

        order.setStockConfirmedStatus();
        return true;
        // return orderRepository.getUnitOfWork().saveChanges();
    }

}
