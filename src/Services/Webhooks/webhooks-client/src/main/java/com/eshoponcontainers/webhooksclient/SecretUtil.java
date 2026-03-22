package com.eshoponcontainers.webhooksclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SecretUtil {

    @Autowired
    private Environment env; 

    public String readSecret(String secretKey) {
        try {
            // .trim() is crucial because K8s secret files often have a trailing newline
            String fullPath = env.getProperty("SECRETS_PATH", "/vault/secrets") + "/" + secretKey;
            return Files.readString(Paths.get(fullPath)).trim();
        } catch (IOException e) {
            throw new RuntimeException("Could not read secret: " + secretKey, e);
        }
    }
}
