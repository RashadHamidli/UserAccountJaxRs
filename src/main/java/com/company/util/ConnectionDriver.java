package com.company.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectionDriver {

    private static final EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("jaxRsTaskPU");
    }

    public static EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }

    public static void closeConnection(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    public static void closeFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static void transactionRollBack(EntityManager entityManager) {
        if (entityManager != null && entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}
