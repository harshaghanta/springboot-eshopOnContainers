package com.eshoponcontainers.orderapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
@EnableJpaRepositories(basePackages = { "com.eshoponcontainers.repositories"
})
// @EntityScan(basePackages = { "com.eshoponcontainers.entities" })
@EnableTransactionManagement(order = 0)
@EnableAutoConfiguration(exclude = {
		// PersistenceExceptionTranslationAutoConfiguration.class,
		// HibernateJpaAutoConfiguration.class,
		DataSourceAutoConfiguration.class })
@EnableScheduling
public class OrderapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderapiApplication.class, args);
	}

	// @Bean
	// @Primary
	// public EntityManager entityManager(EntityManagerFactory factory) {
	// return factory.createEntityManager();
	// }

	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(Environment env) {

		String secretsPath = env.getProperty("SECRETS_PATH", "/vault/secrets");
		String username = readSecret(secretsPath + "/DB_USER_NAME");
		String password = readSecret(secretsPath + "/DB_PASSWORD");

		String hostName = env.getProperty("DB_HOST_NAME");
		String hostPort = env.getProperty("DB_HOST_PORT");
		String dbName = env.getProperty("ORDER_DB_NAME");
		String datasourceUrl = "jdbc:sqlserver://" + hostName + ":" + hostPort + ";databaseName=" +
				dbName + ";integratedSecurity=false;encrypt=false;trustServerCertificate=true;";

		Map<String, String> persistenceMap = new HashMap<String, String>();

		persistenceMap.put("jakarta.persistence.jdbc.url", datasourceUrl);
		persistenceMap.put("jakarta.persistence.jdbc.user", username);
		persistenceMap.put("jakarta.persistence.jdbc.password", password);
		persistenceMap.put("jakarta.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("ordering-persistence", persistenceMap);
		return emFactory;
	}


	private String readSecret(String filePath) {
		try {
			// .trim() is crucial because K8s secret files often have a trailing newline
			return Files.readString(Paths.get(filePath)).trim();
		} catch (IOException e) {
			throw new RuntimeException("Could not read database secret at " + filePath, e);
		}
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
