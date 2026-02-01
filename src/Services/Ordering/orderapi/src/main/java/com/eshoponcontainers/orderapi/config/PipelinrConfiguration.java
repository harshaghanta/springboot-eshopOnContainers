package com.eshoponcontainers.orderapi.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;

@Configuration
public class PipelinrConfiguration {

    @Bean
    Pipeline pipeline(
        ObjectProvider<Command.Handler> commandHandlers,
        ObjectProvider<Notification.Handler> notificationHandlers,
        ObjectProvider<Command.Middleware> commandMiddlewares
        // If you have Notification.Middleware, add: ObjectProvider<Notification.Middleware> notificationMiddlewares
    ) {
        return new Pipelinr()
            .with(() -> commandHandlers.stream())
            .with(() -> notificationHandlers.stream())
            .with(() -> commandMiddlewares.stream());
            // If you have Notification.Middleware, add:
            // .with(() -> notificationMiddlewares.stream());
    }

}
