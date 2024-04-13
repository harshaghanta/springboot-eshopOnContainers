package com.eshoponcontainers.webhooksclient.services;

import org.springframework.stereotype.Service;

import com.eshoponcontainers.webhooksclient.Settings;

@Service
public class SettingsService {

    public Settings getSettings() {
        Settings settings = new Settings();
        return settings;
    }
}
