package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.application.commands.SetPaidOrderStatusCommand;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetPaidOrderStatusCommandHandler implements Command.Handler<SetPaidOrderStatusCommand, Boolean> {
    
    private final IOrderRepository orderRepository;
    @Override
    public Boolean handle(SetPaidOrderStatusCommand command) {
        // Simulate a work time for validating the payment
        // try {
        //     Thread.sleep(10000);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        Order order = orderRepository.get(command.getOrderNumber());
        if(order == null) {
            log.warn("Order with Id: {} not found", command.getOrderNumber());
            return false;
        }
            
        
        order.SetPaidStatus();
        return orderRepository.getUnitOfWork().saveChanges();
    }

}
