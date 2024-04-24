package com.eshoponcontainers.webhooksapi.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "Subscriptions")
@ToString
public class WebhookSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String destUrl;    
    private LocalDateTime date;
    private String token;
    private String userId;
    private WebhookType type;
}
