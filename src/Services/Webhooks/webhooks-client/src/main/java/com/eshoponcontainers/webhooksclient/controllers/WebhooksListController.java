package com.eshoponcontainers.webhooksclient.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.eshoponcontainers.webhooksclient.models.WebhookResponse;
import com.eshoponcontainers.webhooksclient.services.WebhooksClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebhooksListController {

    private final WebhooksClient webhooksClient;

    @GetMapping("/WebhooksList")
    public ModelAndView get() {
        List<WebhookResponse> webhooks = webhooksClient.loadWebhooks();
        ModelAndView modelAndView = new ModelAndView("webhookslist");
        modelAndView.addObject("webhooks", webhooks);
        return modelAndView;
    }

}
