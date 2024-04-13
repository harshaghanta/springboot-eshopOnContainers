package com.eshoponcontainers.webhooksclient.filters;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import com.eshoponcontainers.webhooksclient.utils.AccessTokenUtils;

import reactor.core.publisher.Mono;

public class WebClientAuthorizationFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        
        String accessToken = AccessTokenUtils.getAccessToken();
        if(accessToken != null)
            request.headers().setBearerAuth(accessToken);
        return next.exchange(request);
    }

}
