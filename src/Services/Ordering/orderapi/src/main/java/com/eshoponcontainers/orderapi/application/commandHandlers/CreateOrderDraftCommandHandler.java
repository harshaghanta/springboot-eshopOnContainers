package com.eshoponcontainers.orderapi.application.commandHandlers;

import java.util.List;
import java.util.stream.Collectors;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderDraftCommand;
import com.eshoponcontainers.orderapi.application.viewModels.OrderDraftDTO;
import com.eshoponcontainers.orderapi.application.viewModels.OrderItemDTO;

import an.awesome.pipelinr.Command;

public class CreateOrderDraftCommandHandler implements Command.Handler<CreateOrderDraftCommand, OrderDraftDTO> {

    @Override
    public OrderDraftDTO handle(CreateOrderDraftCommand command) {
        
        Order order = Order.newDraft();
        List<OrderItemDTO> orderItems = command.getItems().stream().
            map(bi -> new OrderItemDTO(bi.productId(), bi.productName(), bi.unitPrice(), 0, bi.quantity(), bi.pictureUrl()))
            .collect(Collectors.toList());
        for (OrderItemDTO orderItem : orderItems) {
                order.addOrderItem(orderItem.productId(), orderItem.productName(), orderItem.unitPrice(),orderItem.discount(),orderItem.pictureUrl());            
        }

        return OrderDraftDTO.FromOrder(order);

    }

}
