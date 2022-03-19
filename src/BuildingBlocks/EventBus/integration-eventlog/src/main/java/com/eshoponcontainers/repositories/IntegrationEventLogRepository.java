package com.eshoponcontainers.repositories;

import java.util.List;
import java.util.UUID;

import com.eshoponcontainers.EventStateEnum;
import com.eshoponcontainers.entities.IntegrationEventLogEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationEventLogRepository extends JpaRepository<IntegrationEventLogEntry, UUID> {

    List<IntegrationEventLogEntry> findByTransactionIdAndState(String transactionId, EventStateEnum eventState);

}