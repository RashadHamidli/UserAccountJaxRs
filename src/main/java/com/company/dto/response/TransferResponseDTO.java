package com.company.dto.response;



import com.company.entity.Account;
import com.company.entity.Transaction;

import java.time.format.DateTimeFormatter;

public record TransferResponseDTO(String transactionNumber,
                                  String fromAccount,
                                  String toAccount,
                                  Double amount,
                                  Double fromBalance,
                                  Double toBalance,
                                  String createAt,
                                  String status) {
    public static TransferResponseDTO fromTransfer(Transaction transaction, Account fromAccount, Account toAccount) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return new TransferResponseDTO(
                transaction.getTransactionNumber(),
                fromAccount.getAccountNumber(),
                toAccount.getAccountNumber(),
                transaction.getAmount(),
                fromAccount.getBalance(),
                toAccount.getBalance(),
                transaction.getTransactionDate().format(formatter),
                "successfully operations!"
        );
    }
}
