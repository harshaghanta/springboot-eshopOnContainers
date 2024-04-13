package com.eshoponcontainers.webhooksapi.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GrantUrlTesterService {    

    public boolean testGrantUrl(String urlHook, String url, String token) {

        if (!checkSameOrigin(urlHook, url)) {
            log.warn("Url of the hook {} and the grant url {} do not belong to same origin", urlHook, url);
            return false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-eshop-whtoken", token)
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
        log.info("Sending the OPTIONS message to {} with token {}", url, token);

        try {
            HttpResponse<Void> response =  HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding());
            HttpHeaders headers = response.headers();
            List<String> tokenValues = headers.allValues("X-eshop-whtoken");
            String tokenReceived = tokenValues.isEmpty() ? null : tokenValues.get(0);
            String tokenExpected = token == null || token.isBlank() ? null : token;

            log.info("Response code is {} for URL {} and token in header was {} (expected token was {})",
                    response.statusCode(), url, tokenReceived, tokenExpected);

            boolean isSuccess = response.statusCode() == 200 && tokenReceived != null
                    && tokenReceived.equals(tokenExpected);
            return isSuccess;
        } catch (Exception ex) {
            log.error("Exception " + ex.getClass().getSimpleName()
                    + " when sending OPTIONS request. URL can't be granted.");
            return false;
        }
    }

    private boolean checkSameOrigin(String urlHook, String url) {
        URI firstUrl = URI.create(urlHook);
        URI secondUrl = URI.create(url);

        return firstUrl.getScheme().equals(secondUrl.getScheme()) &&
                firstUrl.getPort() == secondUrl.getPort() &&
                firstUrl.getHost().equals(secondUrl.getHost());
    }

}
