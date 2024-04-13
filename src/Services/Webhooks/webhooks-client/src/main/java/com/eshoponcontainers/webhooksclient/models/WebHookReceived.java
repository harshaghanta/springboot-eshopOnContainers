package com.eshoponcontainers.webhooksclient.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebHookReceived {

    private LocalDateTime when;
    private String data;
    private String token;
}
