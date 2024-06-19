package com.company.exeption;

import com.company.util.ConnectionDriver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

public class CustomException {
    public static void handleOperationException(Exception e, EntityManager entityManager) throws TransactionException, AccountNotFoundException {
        if (e instanceof TransactionException transactionException) {
            ConnectionDriver.transactionRollBack(entityManager);
            System.out.printf("\u001B[31m%s\u001B[0m%n", transactionException.getMessage());
            throw transactionException;
        } else if (e instanceof AccountNotFoundException accountNotFoundException) {
            ConnectionDriver.transactionRollBack(entityManager);
            System.out.printf("\u001B[31m%s\u001B[0m%n", accountNotFoundException.getMessage());
            throw accountNotFoundException;
        } else if (e instanceof UserNotFoundException userNotFoundException) {
            ConnectionDriver.transactionRollBack(entityManager);
            System.out.printf("\u001B[31m%s\u001B[0m%n", userNotFoundException.getMessage());
            throw userNotFoundException;
        } else {
            handleUnexpectedException(e, entityManager);
        }
    }

    public static void handlePersistenceException(PersistenceException e, EntityManager entityManager) {
        ConnectionDriver.transactionRollBack(entityManager);
        System.out.printf("\u001B[31mDatabase error occurred: %s\u001B[0m%n", e.getMessage());
        throw new RuntimeException("Database error occurred", e);
    }

    public static void handleUnexpectedException(Exception e, EntityManager entityManager) {
        ConnectionDriver.transactionRollBack(entityManager);
        System.out.printf("\u001B[31mUnexpected error occurred: %s\u001B[0m%n", e.getMessage());
        throw new RuntimeException("An unexpected error occurred", e);
    }
}
