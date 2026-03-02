package com.eshoponcontainers.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Outbox")
public class OutboxEntity {

    @Id
    private UUID id;
    private String eventTypeName;
    private String content;
    private String status;
    private LocalDateTime lastAttemptedAt;
    private int retryCount;

}
