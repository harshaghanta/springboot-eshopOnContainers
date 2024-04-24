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
public class WebHookReceived {

    private LocalDateTime when;
    private String data;
    private String token;
}
