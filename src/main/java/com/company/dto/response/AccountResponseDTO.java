package com.company.dto.response;

import com.company.entity.Account;

public record AccountResponseDTO(String username,
                                 String accountName,
                                 String accountNumber,
                                 Double balance,
                                 Boolean isActive) {

    public static AccountResponseDTO fromAccount(Account account) {
        return new AccountResponseDTO(account.getUser().getUsername(),
                account.getAccountName(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getIsActive());
    }
}
