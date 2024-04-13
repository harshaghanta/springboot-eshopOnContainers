package com.eshoponcontainers.webhooksapi.controllers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.webhooksapi.dto.WebhookSubscriptionRequest;
import com.eshoponcontainers.webhooksapi.entities.WebhookSubscription;
import com.eshoponcontainers.webhooksapi.entities.WebhookType;
import com.eshoponcontainers.webhooksapi.repositories.WebhookSubscritpionRepository;
import com.eshoponcontainers.webhooksapi.services.GrantUrlTesterService;
import com.eshoponcontainers.webhooksapi.services.IdentityService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks")
public class WebhooksController {

    private final WebhookSubscritpionRepository webhookSubscritpionRepository;
    private final IdentityService identityService;
    private final GrantUrlTesterService grantUrlTesterService;

    @GetMapping
    public ResponseEntity<?> listByUser() {
        String userId = identityService.getUserId();
        List<WebhookSubscription> subscriptions = webhookSubscritpionRepository.findByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByUserAndId(@PathVariable Integer id) {
        String userId = identityService.getUserId();
        WebhookSubscription subscription = webhookSubscritpionRepository.findByIdAndUserId(id, userId);
        if (subscription != null)
            return ResponseEntity.ok(subscription);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Subscriptions %s not found", id));
    }

    @PostMapping
    public ResponseEntity<?> subscribeWebhook(@RequestBody WebhookSubscriptionRequest request) {
        String token = "";
        if (request.getToken() != null)
            token = request.getToken();
        var grantOk = grantUrlTesterService.testGrantUrl(request.getUrl(), request.getGrantUrl(), token);
        if (grantOk) {
            var subscription = new WebhookSubscription();
            subscription.setDate(LocalDateTime.now());
            subscription.setDestUrl(request.getUrl());
            //  convert request.getType() to WebhookType
            
            subscription.setType(WebhookType.valueOf(request.getEvent()));
            subscription.setToken(token);
            subscription.setUserId(identityService.getUserId());
            var savedSubscription = webhookSubscritpionRepository.save(subscription);
            URI uri = URI.create("/" + savedSubscription.getId());
            return ResponseEntity.created(uri).build();
        } else {
            return ResponseEntity.status(418).body("Grant url can't be validated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unsubscribeWebhook(@PathVariable Integer id) {
        String userId = identityService.getUserId();
        var subscription = webhookSubscritpionRepository.findByIdAndUserId(id, userId);
        if (subscription != null) {
            webhookSubscritpionRepository.delete(subscription);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Subscriptions %s not found", id));
    }
}
