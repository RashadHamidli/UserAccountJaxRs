package com.company.dto.response;

import com.company.entity.Transaction;
import com.company.entity.TransactionType;

import java.time.format.DateTimeFormatter;

public record TransactionResponseDTO(String transactionNumber,
                                     TransactionType transactionType,
                                     Double newBalance,
                                     String transactionDate,
                                     String toAccountNumber,
                                     String fromAccountNumber) {
    public static TransactionResponseDTO fromTransaction(Transaction transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return new TransactionResponseDTO(transaction.getTransactionNumber(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getTransactionDate().format(formatter),
                transaction.getToAccount().getAccountNumber(),
                transaction.getFromAccount().getAccountNumber());
    }
}
