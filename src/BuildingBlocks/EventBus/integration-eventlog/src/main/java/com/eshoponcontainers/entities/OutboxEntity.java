package com.eshoponcontainers.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Outbox")
@Getter
public class OutboxEntity {

    @Id
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private UUID id;
    @Column(name = "EventTypeName", columnDefinition = "nvarchar(500)")
    private String eventTypeName;
    @Column(name = "Content", columnDefinition = "nvarchar(max)")
    private String content;
    @Column(name = "Status", columnDefinition = "varchar(50)")
    private String status;
    @Column(name = "LockedBy", columnDefinition = "nvarchar(200)")
    private String lockedBy;
    @Column(name = "LastAttemptedAt", columnDefinition = "datetime2")
    private LocalDateTime lastAttemptedAt;
    @Column(name = "RetryCount", columnDefinition = "int")
    private int retryCount;

}
