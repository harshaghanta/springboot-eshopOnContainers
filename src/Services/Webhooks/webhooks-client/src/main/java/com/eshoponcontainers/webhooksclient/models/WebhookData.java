package com.eshoponcontainers.webhooksclient.models;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class WebhookData {

    private LocalDateTime when;
    private String payload;
    private String type;
}
