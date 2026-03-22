package com.eshoponcontainers.orderingbackgroundtasks.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DbConfig {

    @Bean
    public DataSource dataSource(Environment env) {


        String secretsPath = env.getProperty("SECRETS_PATH", "/vault/secrets");
        String username = readSecret(secretsPath + "/DB_USER_NAME");
        String password = readSecret(secretsPath + "/DB_PASSWORD");
        String hostName = env.getProperty("DB_HOST_NAME");
        String hostPort = env.getProperty("DB_HOST_PORT");
        String dbName = env.getProperty("ORDER_DB_NAME");
        String datasourceUrl = "jdbc:sqlserver://" + hostName + ":" + hostPort + ";databaseName=" +
            dbName + ";integratedSecurity=false;encrypt=false;trustServerCertificate=true;";

        return DataSourceBuilder.create()
            .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            .url(datasourceUrl)
            .username(username)
            .password(password)
            .build();
    }

    private String readSecret(String filePath) {
        try {
            // .trim() is crucial because K8s secret files often have a trailing newline
            return Files.readString(Paths.get(filePath)).trim();
        } catch (IOException e) {
            throw new RuntimeException("Could not read database secret at " + filePath, e);
        }
    }
}
