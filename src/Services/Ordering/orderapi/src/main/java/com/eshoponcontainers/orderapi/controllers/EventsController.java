package com.eshoponcontainers.orderapi.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.eventbus.SubscriptionInfo;
import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class EventsController {

    private final EventBusSubscriptionManager eventBusSubscriptionManager;

    @GetMapping("/subscriptions")
    public HashMap<String, List<String>> getSubscriptions() {
        HashMap<String, List<SubscriptionInfo>> allSubscriptions = eventBusSubscriptionManager.getAllSubscriptions();
        HashMap<String, List<String>> subscriptions = new HashMap<>();
        allSubscriptions.entrySet().stream().forEach(entry -> {
            List<String> handlers = entry.getValue().stream().map(subscription -> subscription.getHandler().getName()).toList();
            subscriptions.put(entry.getKey(), handlers);
        });
        return subscriptions;
    }
    
}
