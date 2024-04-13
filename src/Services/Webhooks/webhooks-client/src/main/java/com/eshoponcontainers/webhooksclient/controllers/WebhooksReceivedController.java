package com.eshoponcontainers.webhooksclient.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.webhooksclient.Settings;
import com.eshoponcontainers.webhooksclient.models.WebHookReceived;
import com.eshoponcontainers.webhooksclient.models.WebhookData;
import com.eshoponcontainers.webhooksclient.services.HooksRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/webhook-received")

public class WebhooksReceivedController {

    private static final String WEBHOOKCHECKHEADER = "X-eshop-whtoken";
    private final HooksRepository hooksRepository;
    private final Settings settings;

    @PostMapping
    public ResponseEntity<?> newWebhook(@RequestHeader(value = WEBHOOKCHECKHEADER, required = false) String token,
            WebhookData hook) {

        log.info("Received hook with token {}. My token is {}. Token validation is set to {}", token,
                settings.getToken(), settings.isValidateToken());

        if (!settings.isValidateToken() || settings.getToken().equals(token)) {
            log.info("Received hook is going to be processed");
            var newHook = new WebHookReceived(hook.getWhen(), hook.getPayload(), token);

            hooksRepository.addNew(newHook);
            log.info("Received hook was processed.");
            return ResponseEntity.ok(newHook);
        }

        log.info("Received hook is NOT processed - Bad Request returned.");
        return ResponseEntity.badRequest().build();
    }
}
