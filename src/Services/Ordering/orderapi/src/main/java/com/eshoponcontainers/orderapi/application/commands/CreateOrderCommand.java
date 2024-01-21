package com.eshoponcontainers.orderapi.application.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.eshoponcontainers.orderapi.application.viewModels.OrderItemDTO;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderCommand implements Command<Boolean>  {

    private List<OrderItemDTO> orderItems;
    private String userId;
    private String userName;
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipCode;
    private String cardNumber;
    private LocalDate cardExpiration;
    private String cardSecurityNumber;
    private int cardTypeId;
    
    public CreateOrderCommand() {
        orderItems = new ArrayList<OrderItemDTO>();
    }
}
