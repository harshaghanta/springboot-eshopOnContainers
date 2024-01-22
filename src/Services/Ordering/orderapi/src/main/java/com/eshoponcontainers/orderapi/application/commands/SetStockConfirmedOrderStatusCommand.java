package com.eshoponcontainers.orderapi.application.commands;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetStockConfirmedOrderStatusCommand implements Command<Boolean> {

    private int orderNumber;
}
