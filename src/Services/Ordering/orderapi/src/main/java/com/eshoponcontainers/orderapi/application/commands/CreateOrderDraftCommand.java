package com.eshoponcontainers.orderapi.application.commands;

import java.util.List;

import com.eshoponcontainers.orderapi.application.viewModels.BasketItem;
import com.eshoponcontainers.orderapi.application.viewModels.OrderDraftDTO;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrderDraftCommand implements Command<OrderDraftDTO> {

    private String buyerId;
    private List<BasketItem> items;
}
