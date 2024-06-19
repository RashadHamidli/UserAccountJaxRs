package com.company.exeption;

import lombok.Getter;

@Getter
public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String failedToCreateTransaction, Exception e) {
        super(failedToCreateTransaction, e);
    }
}

