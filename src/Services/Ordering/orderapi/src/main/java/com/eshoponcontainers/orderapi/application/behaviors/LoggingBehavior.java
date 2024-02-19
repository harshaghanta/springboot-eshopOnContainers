package com.eshoponcontainers.orderapi.application.behaviors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import an.awesome.pipelinr.Command;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(100)
public class LoggingBehavior implements Command.Middleware {
    
    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        log.info("----- Handling command {} ({})", command.getClass().getSimpleName(), command);
        R response = next.invoke();
        log.info("----- Command {} handled ", command.getClass().getSimpleName());
        return response;
    }
}
