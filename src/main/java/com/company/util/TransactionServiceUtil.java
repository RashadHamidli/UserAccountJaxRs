package com.company.util;

import com.company.entity.Account;
import com.company.entity.Transaction;
import com.company.entity.TransactionType;
import com.company.exeption.TransactionException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionServiceUtil {
    public static Transaction createTransaction(Account fromAccount, Account toAccount, double amount) throws TransactionException {
        try {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Transaction transaction = new Transaction();
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setAmount(amount);
            transaction.setTransactionType(TransactionType.TRANSFER);
            transaction.setFromAccount(fromAccount);
            transaction.setToAccount(toAccount);
            transaction.setTransactionNumber(AccountServiceUtil.generateAccountNumber());

            return transaction;
        } catch (Exception e) {
            throw new TransactionException("Failed to create transaction", e);
        }
    }

    public static List<Transaction> findTransactions(EntityManager entityManager, Account account) throws TransactionException {
        try {
            return entityManager.createQuery("from Transaction t where t.fromAccount=:fromAccount", Transaction.class)
                    .setParameter("fromAccount", account).getResultList();
        } catch (NoResultException e) {
            throw new TransactionException("Transaction list not found");
        }
    }

    public static Transaction findTransactionByTransactionNumber(EntityManager entityManager, String transactionNumber) throws TransactionException {
        try {
            return entityManager.createQuery("from Transaction n where n.transactionNumber=:transactionNumber", Transaction.class)
                    .setParameter(transactionNumber, transactionNumber).getSingleResult();
        } catch (NoResultException e) {
            throw new TransactionException("Transaction not found");
        }
    }

    public static List<Transaction> findTransactionsForAccounts(EntityManager entityManager, List<Account> accountList) throws TransactionException {
        try {
            return entityManager.createQuery("from Transaction t where t.fromAccount IN :accounts OR t.toAccount IN :accounts", Transaction.class)
                    .setParameter("accounts", accountList).getResultList();
        } catch (NoResultException e) {
            throw new TransactionException("Transaction list not found");
        }
    }

    public static Transaction findTransactionByAccount(EntityManager entityManager, Account account) throws TransactionException {
        try {
            return entityManager.createQuery("from Transaction n where n.fromAccount=:fromAccount", Transaction.class)
                    .setParameter("fromAccount", account).getSingleResult();
        } catch (NoResultException e) {
            throw new TransactionException("Transaction not found");
        }
    }
}
