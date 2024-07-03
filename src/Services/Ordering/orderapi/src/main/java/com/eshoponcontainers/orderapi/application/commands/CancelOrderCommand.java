package com.eshoponcontainers.orderapi.application.commands;

import com.fasterxml.jackson.annotation.JsonAlias;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderCommand implements Command<Boolean> {
    
    @JsonAlias({"orderNumber", "OrderNumber"})
    private int orderNumber;

}
