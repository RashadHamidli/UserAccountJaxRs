package com.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String transactionNumber;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private LocalDateTime transactionDate;
    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;
    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;
}
