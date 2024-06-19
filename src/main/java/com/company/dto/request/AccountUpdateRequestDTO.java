package com.company.dto.request;

import com.company.entity.Account;

public record AccountUpdateRequestDTO(String accountName,
                                      String accountNumber,
                                      Double balance,
                                      Boolean isActive) {
    public static Account toAccount(AccountUpdateRequestDTO accountUpdateRequestDTO) {
        return Account.builder().accountName(accountUpdateRequestDTO.accountName()).
                accountNumber(accountUpdateRequestDTO.accountNumber()).
                balance(accountUpdateRequestDTO.balance()).
                isActive(accountUpdateRequestDTO.isActive()).build();
    }
}
