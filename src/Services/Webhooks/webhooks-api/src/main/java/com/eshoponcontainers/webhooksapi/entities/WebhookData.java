package com.eshoponcontainers.webhooksapi.entities;

import java.time.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class WebhookData {

    private LocalDateTime when;
    private String payload;
    private String type;

    public WebhookData(WebhookType hookType, Object data) {
        this.when = LocalDateTime.now();
        this.type = hookType.toString();
        try {
            payload = new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
