package com.eshoponcontainers.entities;

import java.util.Date;
import java.util.UUID;



import com.eshoponcontainers.EventStateEnum;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;

@Entity
@Table(name = "IntegrationEventLog")
@Getter
public class IntegrationEventLogEntry {
    
    @Id
    @Column(name = "EventId", columnDefinition = "uniqueidentifier")
    private UUID eventId;

    @Column(name = "Content", nullable = false, columnDefinition = "nvarchar(MAX)")
    private String content;

    @Column(name = "CreationTime", nullable = false)
    private Date creationTime;

    @Column(name = "EventTypeName", nullable = false, columnDefinition = "nvarchar(MAX)")
    private String eventTypeName;

    @Column(name = "State", nullable = false)
    private EventStateEnum state;

    @Column(name =  "TimesSent", nullable = false)
    private Integer timesSent;

    @Column(name ="TransactionId", nullable = true, columnDefinition = "nvarchar(MAX)")
    private String transactionId;

    @Transient
    private IntegrationEvent event;

    //Added this as a constructor to avoid the error during save of entity. Seems hibernate needs a default constructor, but does't care if its public:
    private IntegrationEventLogEntry() {

    }

    public IntegrationEventLogEntry(IntegrationEvent event, UUID transId) {
        eventId = event.getId();
        creationTime = event.getCreationDate();
        eventTypeName = event.getClass().getName();
      
        try {
            content = new ObjectMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {            
            e.printStackTrace();
        }
        state = EventStateEnum.NOT_PUBLISHED;
        timesSent = 0;
        transactionId = transId.toString();
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public EventStateEnum getState() {
        return state;
    }

    public Integer getTimesSent() {
        return timesSent;
    }

    public void setTimesSent(Integer timesSent) {
        this.timesSent = timesSent;
    }

    public void setState(EventStateEnum state) {
        this.state = state;
    }

    public void deserializeEventContent() {
        this.event = new ObjectMapper().convertValue(this.content, IntegrationEvent.class);
    }
    
}
