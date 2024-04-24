package com.eshoponcontainers.webhooksclient.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebhookData {

    private LocalDateTime when;
    private String payload;
    private String type;
}
