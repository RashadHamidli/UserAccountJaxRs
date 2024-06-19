package com.company.dto.request;

import com.company.entity.Account;

public record AccountCreateRequestDTO(String username,
                                      String accountName) {

    public static Account toAccount(AccountCreateRequestDTO accountCreateRequestDTO) {
        return Account.builder().accountName(accountCreateRequestDTO.accountName()).build();
    }
}
