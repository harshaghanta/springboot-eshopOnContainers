package com.eshoponcontainers.webhooksclient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookSubscriptionRequest {

    private String url;
    private String token;
    private String event;
    private String grantUrl;
}
