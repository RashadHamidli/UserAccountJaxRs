package com.company.dto.request;

public record TransferRequestDTO(String fromAccount,
                                 String toAccount,
                                 Double amount) {
}
