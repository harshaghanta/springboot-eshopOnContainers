package com.eshoponcontainers.eventbus.events;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;

@Getter
public class IntegrationEvent {

    private UUID id;
    private Date creationDate;

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
    }
}
