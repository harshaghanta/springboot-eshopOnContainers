package com.eshoponcontainers.webhooksclient.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.eshoponcontainers.webhooksclient.Settings;
import com.eshoponcontainers.webhooksclient.models.WebhookSubscriptionRequest;
import com.eshoponcontainers.webhooksclient.utils.AccessTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/RegisterWebhook")
public class RegisterWebhookController {

    private final Settings settings;

    @Qualifier("grantClientWebClient")
    private final WebClient webClient;

    @GetMapping
    public String get(Model model) {
        model.addAttribute("token", "6168DB8D-DC58-4094-AF24-483278923590");
        model.addAttribute("responseCode", 200);
        return "registerwebhook";
    }

    @PostMapping
    public ModelAndView send(@RequestBody String entity, BindingResult result, HttpServletRequest servletRequest) {

        String selfUrl = settings.getSelfUrl();
        if (selfUrl.isEmpty() || selfUrl == null)
            selfUrl = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort()
                    + servletRequest.getContextPath();

        if (!selfUrl.endsWith("/"))
            selfUrl = selfUrl + "/";
        var granturl = selfUrl + "checkpost";
        var url = selfUrl + "webhook-received";
        
        var payload = new WebhookSubscriptionRequest(url, settings.getToken(), "OrderPaid", granturl);

        class ResponseData {
            boolean isSuccess;
            int responseCode;
            String method;
            String requestUri;
            String responseMessage;
        }

        ResponseData responseData = new ResponseData();

        // Set the access token
        WebClient.create(settings.getWebhookUrl())
                .post()
                .uri("/api/v1/webhooks")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + AccessTokenUtils.getAccessToken())
                .bodyValue(payload)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        responseData.isSuccess = true;
                    } else {
                        String reasonPhrase = "";
                        if (response.statusCode().is4xxClientError())
                            reasonPhrase = "Client error";
                        else if (response.statusCode().is5xxServerError())
                            reasonPhrase = "Server error";
                        else
                            reasonPhrase = "Unknown error";
                        responseData.isSuccess = false;
                        responseData.responseCode = response.statusCode().value();
                        responseData.method = response.request().getMethod().name();
                        responseData.requestUri = response.request().getURI().toString();
                        responseData.responseMessage = reasonPhrase;
                    }
                    return Mono.empty();
                }).block();

        if (responseData.isSuccess) {
            return new ModelAndView("redirect:/webhookslist");
        } else {
            var error = new ObjectError("webhookerror", "errormessage");
            result.addError(error);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("registerwebhook");
            try {
                modelAndView.addObject("requestBodyJson", new ObjectMapper().writeValueAsString(payload));
            } catch (JsonProcessingException e) {
            }
            
            modelAndView.addObject("responseCode", responseData.responseCode);
            modelAndView.addObject("method", responseData.method);
            modelAndView.addObject("requestUri", responseData.requestUri);
            modelAndView.addObject("grantUrl", granturl);
            modelAndView.addObject("responseMessage", responseData.responseMessage);
            return modelAndView;
        }
    }
}