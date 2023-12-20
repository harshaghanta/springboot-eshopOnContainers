package com.eshoponcontainers.orderapi.application.commandHandlers;

import com.eshoponcontainers.orderapi.application.commands.CancelOrderCommand;

import an.awesome.pipelinr.Command;

public class CancelOrderCommandHandler implements Command.Handler<CancelOrderCommand, Boolean> {

    @Override
    public Boolean handle(CancelOrderCommand command) {
        
        return true;
    }

}
