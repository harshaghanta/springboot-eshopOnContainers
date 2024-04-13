package com.eshoponcontainers.webhooksclient.services;

import java.util.List;

import com.eshoponcontainers.webhooksclient.models.WebHookReceived;

public interface HooksRepository {

    List<WebHookReceived> getAll();

    void addNew(WebHookReceived hook);
}
