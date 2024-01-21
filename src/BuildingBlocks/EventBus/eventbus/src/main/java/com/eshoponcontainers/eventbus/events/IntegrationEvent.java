package com.eshoponcontainers.eventbus.events;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.OptBoolean;

import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type" , requireTypeIdForSubtypes = OptBoolean.FALSE )
public class IntegrationEvent {

    private UUID id;
    private Date creationDate;

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
    }
}
