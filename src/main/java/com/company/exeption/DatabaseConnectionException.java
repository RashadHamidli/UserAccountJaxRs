package com.company.exeption;

public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
