package com.eshoponcontainers.config;

import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {
    private static EntityManagerFactory EMF;
    private static final ThreadLocal<EntityManager> THREAD_LOCAL_EM = new ThreadLocal<>();

    public static void intializeEntityManagerFactory(Map<String, String> properties) {
        Persistence.createEntityManagerFactory("ordering-persistence", properties );
    }

    public static EntityManager getEntityManager() {
        EntityManager em = THREAD_LOCAL_EM.get();
        if (em == null || !em.isOpen()) {
            em = EMF.createEntityManager();
            THREAD_LOCAL_EM.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = THREAD_LOCAL_EM.get();
        if (em != null) {
            em.close();
            THREAD_LOCAL_EM.remove();
        }
    }
}