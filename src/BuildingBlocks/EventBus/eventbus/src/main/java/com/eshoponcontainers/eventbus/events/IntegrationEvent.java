package com.eshoponcontainers.eventbus.events;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
// @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type" , requireTypeIdForSubtypes = OptBoolean.FALSE )
public class IntegrationEvent {

    private UUID id;
    private Date creationDate;

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
    }
}
