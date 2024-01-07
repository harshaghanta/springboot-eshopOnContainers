package com.eshoponcontainers.orderapi.application.viewModels;

import java.util.List;
import java.util.stream.Collectors;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;

public class OrderDraftDTO {

    private List<OrderItemDTO> orderItems;
    private double total;
    
    public static OrderDraftDTO FromOrder(Order order) {
        OrderDraftDTO orderDraftDTO = new OrderDraftDTO();
        List<OrderItemDTO> orderItems = order.getOrderItems().stream().map(
            oi -> new OrderItemDTO(0, oi.getProductName(), oi.getUnitPrice() , oi.getDiscount(), oi.getUnits(), oi.getPictureUrl())
        ).collect(Collectors.toList());
        orderDraftDTO.orderItems = orderItems;
        orderDraftDTO.total = order.getTotal();
        return orderDraftDTO;
    }

}
