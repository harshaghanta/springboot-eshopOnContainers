package com.eshoponcontainers.orderapi.application.commands;

import java.util.List;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SetStockRejectedOrderStatusCommand  implements Command<Boolean> {

    private int orderNumber;
    private List<Integer> orderStockItems;
}
