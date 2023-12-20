package com.eshoponcontainers.orderapi.application.commands;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderCommand implements Command<Boolean> {

    private int orderNumber;

}
