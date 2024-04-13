package com.eshoponcontainers.webhooksclient.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eshoponcontainers.webhooksclient.models.WebHookReceived;

@Repository
public class InMemoryHooksRepository implements HooksRepository {

    private final List<WebHookReceived> webhooks = new ArrayList<>();

    @Override
    public List<WebHookReceived> getAll() {
        return webhooks;
    }

    @Override
    public void addNew(WebHookReceived hook) {
        webhooks.add(hook);
    }

}
