package com.eshoponcontainers.webhooksclient.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WebhookResponse {

    private LocalDateTime date;
    private String destUrl;
    private String token;
}
