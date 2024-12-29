package com.eshoponcontainers.config;

import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityManagerProvider {
    private final EntityManagerFactory entityManagerFactory;
    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    public static void intializeEntityManagerFactory(Map<String, String> properties) {
        Persistence.createEntityManagerFactory("ordering-persistence", properties );
    }

    public EntityManager getEntityManager() {
        EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            threadLocalEntityManager.set(entityManager);
        }
        return entityManager;
    }

    public void closeEntityManager() {
        EntityManager em = threadLocalEntityManager.get();
        if (em != null) {
            em.close();
            threadLocalEntityManager.remove();
        }
    }
}