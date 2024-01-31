package com.eshoponcontainers.orderapi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.eshoponcontainers.orderapi.config.OrderingDBConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
// @EnableJpaRepositories(basePackages = { "com.eshoponcontainers.repositories"
// })
@EnableAutoConfiguration(exclude = { PersistenceExceptionTranslationAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class })
public class OrderapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderapiApplication.class, args);
	}

	@Bean
	@Primary
	public EntityManager entityManager(EntityManagerFactory factory) {
		return factory.createEntityManager();
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(OrderingDBConfig orderingDBConfig) {

		Map<String, String> persistenceMap = new HashMap<String, String>();

		persistenceMap.put("javax.persistence.jdbc.url", orderingDBConfig.getUrl());
		persistenceMap.put("javax.persistence.jdbc.user", orderingDBConfig.getUsername());
		persistenceMap.put("javax.persistence.jdbc.password", orderingDBConfig.getPassword());
		persistenceMap.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("ordering-persistence", persistenceMap);
		return emFactory;
	}

	// @Bean
	// @Primary
	// public PlatformTransactionManager tm(EntityManager em) {
	// final JpaTransactionManager transactionManager = new JpaTransactionManager();
	// transactionManager.setEntityManagerFactory(em.getObject());
	// return transactionManager;
	// }

}
