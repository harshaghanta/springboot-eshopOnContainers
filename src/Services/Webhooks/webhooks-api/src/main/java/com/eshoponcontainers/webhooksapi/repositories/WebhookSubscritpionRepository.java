package com.eshoponcontainers.webhooksapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eshoponcontainers.webhooksapi.entities.WebhookSubscription;
import com.eshoponcontainers.webhooksapi.entities.WebhookType;

public interface WebhookSubscritpionRepository extends JpaRepository<WebhookSubscription, Integer> {

    List<WebhookSubscription> findByUserId(String userId);

    WebhookSubscription findByIdAndUserId(Integer id, String userId);

    List<WebhookSubscription> findByType(WebhookType type);
}
