package com.eshoponcontainers.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.entities.OutboxEntity;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {

    @Transactional
    // @Modifying
    @Query(value = """
            SET NOCOUNT ON;
            UPDATE TOP (20) dbo.Outbox
            SET Status = 'PROCESSING',
                LastAttemptedAt = GETDATE(),
                LockedBy = :podName,
                RetryCount = RetryCount + 1
            OUTPUT inserted.*
            WHERE (Status = 'PENDING' OR (Status = 'PROCESSING' AND LastAttemptedAt < DATEADD(minute, -15, GETDATE())))
              AND RetryCount < :maxRetries
            """, nativeQuery = true)
    // @Procedure(value = "dbo.FetchAndLockOutbox")
    // @Query(value = "SET NOCOUNT ON; EXEC FetchAndLockOutbox :podName, :maxRetries", nativeQuery = true)
    List<OutboxEntity> fetchAndLockBatch(@Param("podName") String podName, @Param("maxRetries") int maxRetries);

    @Transactional
    @Modifying
    @Query("UPDATE OutboxEntity o SET o.status = 'PUBLISHED' WHERE o.id = :id")
    void markAsPublished(@Param("id") UUID id);
}
