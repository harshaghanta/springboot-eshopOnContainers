package com.eshoponcontainers.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Inbox")
@NoArgsConstructor

public class InboxMessage {

    public InboxMessage(UUID id, String eventTypeName) {
        this.id = id;
        this.eventTypeName = eventTypeName;
        this.processedAt = LocalDateTime.now();
    }

    @Id
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "EventTypeName", columnDefinition = "nvarchar(500)")
    private String eventTypeName;

    @Column(name = "ProcessedAt", columnDefinition = "datetime2")
    private LocalDateTime processedAt;

}
