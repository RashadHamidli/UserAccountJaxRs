package com.company.util;

import com.company.entity.Account;
import com.company.entity.User;
import com.company.exeption.AccountNotFoundException;
import com.company.exeption.TransactionException;
import com.company.exeption.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AccountServiceUtil {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final int ACCOUNT_NUMBER_LENGTH = 16;


    public static void saveAccount(Account account) {
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(0.0d);
        account.setCreatedAt(LocalDateTime.now());
        account.setIsActive(true);
    }
    public static void updateAccount(Account foundAccount, Account accountRequest) {
        Optional.ofNullable(accountRequest.getAccountName()).ifPresent(foundAccount::setAccountName);
        Optional.ofNullable(accountRequest.getBalance()).ifPresent(foundAccount::setBalance);
        Optional.ofNullable(accountRequest.getIsActive()).ifPresent(foundAccount::setIsActive);
        foundAccount.setUpdatedAt(LocalDateTime.now());
    }
    public static Account findAccountByAccountNumber(EntityManager entityManager, String accountNumber, String errorMessage) throws AccountNotFoundException {
        try {
            return entityManager.createQuery("from Account t where t.accountNumber=:accountNumber", Account.class)
                    .setParameter("accountNumber", accountNumber).getSingleResult();
        } catch (NoResultException e) {
            throw new AccountNotFoundException(errorMessage);
        }
    }

    public static List<Account> findAccounts(EntityManager entityManager, Boolean isActive) throws AccountNotFoundException {
        try {
            return entityManager.createQuery("from Account t where t.isActive=:isActive", Account.class)
                    .setParameter("isActive", isActive).getResultList();
        } catch (NoResultException e) {
            throw new AccountNotFoundException("account no found");
        }
    }

    public static List<Account> findAccountsByUser(EntityManager entityManager, User user) throws AccountNotFoundException {
        try {
            return entityManager.createQuery("from Account t where t.user=:user", Account.class)
                    .setParameter("user", user).getResultList();
        } catch (NoResultException e) {
            throw new AccountNotFoundException("Account not found");
        }
    }

    public static void validateAccounts(Account fromAccount, Account toAccount, double amount) throws TransactionException {
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new TransactionException("From and To accounts are the same!");
        }
        if (fromAccount.getBalance() < amount) {
            throw new TransactionException(String.format("Insufficient balance! Current balance: %.2f, required amount: %.2f",
                    fromAccount.getBalance(), amount));
        }
    }

    public static Account getSystemAccount() {
        EntityManager entityManager = null;
        Account systemAccount = null;
        User systemUser;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            try {
                systemAccount = findAccountByAccountNumber(entityManager, "SYSTEM", "To Account not found");
            } catch (AccountNotFoundException e) {
                systemUser = User.builder()
                        .name("SYSTEM")
                        .surname("SYSTEM")
                        .createAt(LocalDateTime.now())
                        .email("SYSTEM")
                        .username("SYSTEM")
                        .build();
                entityManager.persist(systemUser);

                systemAccount = Account.builder()
                        .accountName("System Account")
                        .user(systemUser)
                        .accountNumber("SYSTEM")
                        .balance(1000000000.0)
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                entityManager.persist(systemAccount);
            }
            entityManager.getTransaction().commit();
            return systemAccount;
        } catch (Exception e) {
            ConnectionDriver.transactionRollBack(entityManager);
            throw new UserNotFoundException("user not returned", e);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
    }

    public static String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder(ACCOUNT_NUMBER_LENGTH);
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            accountNumber.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH - 2; i++) {
            accountNumber.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return accountNumber.toString();
    }
}
