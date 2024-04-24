package com.eshoponcontainers.webhooksapi.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
