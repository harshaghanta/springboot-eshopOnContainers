package com.eshoponcontainers.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eshoponcontainers.entities.InboxMessage;

public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {

    boolean existsById(UUID id);
}
