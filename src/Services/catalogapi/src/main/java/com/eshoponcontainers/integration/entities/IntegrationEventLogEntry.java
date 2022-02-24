package com.eshoponcontainers.integration.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IntegrationEventLog")
public class IntegrationEventLogEntry {
    
    @Id
    @Column(name = "EventId", columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "Content", nullable = false, columnDefinition = "nvarchar(MAX)")
    private String content;

    @Column(name = "CreationTime", nullable = false)
    private Date creationTime;

    @Column(name = "EventTypeName", nullable = false, columnDefinition = "nvarchar(MAX)")
    private String eventTypeName;

    @Column(name = "State", nullable = false)
    private Integer state;

    @Column(name =  "TimesSent", nullable = false)
    private Integer timesSent;

    @Column(name ="TransactionId", nullable = true, columnDefinition = "nvarchar(MAX)")
    private String transactionId;
    
}
