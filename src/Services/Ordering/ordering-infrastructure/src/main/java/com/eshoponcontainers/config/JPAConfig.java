package com.eshoponcontainers.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Configuration
public class JPAConfig {

    // @Bean
    // public EntityManagerFactory entityManagerFactory() {

    //     EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("ordering-persistence");
    //     return emFactory;
    // }

    // @Bean
    // public EntityManager entityManager() {
    //     return entityManagerFactory().createEntityManager();
    // }


    // @Bean
    // public DataSource dataSource() {
    //     return (DataSource) entityManagerFactory().getProperties().get(org.hibernate.cfg.AvailableSettings.JPA_JTA_DATASOURCE);
    // }

    // @Bean
    // public PlatformTransactionManager transactionManager() {
    //     JpaTransactionManager transactionManager = new JpaTransactionManager();
    //     EntityManagerFactory emFactory = entityManagerFactory();
    //     //transactionManager.setEntityManagerFactory(emFactory);
    //     return transactionManager;
    // }

}
