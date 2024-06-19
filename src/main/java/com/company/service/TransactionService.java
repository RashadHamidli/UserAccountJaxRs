package com.company.service;

import com.company.dto.request.TransactionRequestDTO;
import com.company.dto.request.TransferRequestDTO;
import com.company.dto.response.TransactionResponseDTO;
import com.company.dto.response.TransferResponseDTO;
import com.company.entity.Account;
import com.company.entity.Transaction;
import com.company.entity.User;
import com.company.exeption.AccountNotFoundException;
import com.company.exeption.CustomException;
import com.company.exeption.TransactionException;
import com.company.exeption.UserNotFoundException;
import com.company.util.AccountServiceUtil;
import com.company.util.ConnectionDriver;
import com.company.util.TransactionServiceUtil;
import com.company.util.UserServiceUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class TransactionService {

    private EntityManager entityManager;

    public List<TransactionResponseDTO> getAllTransaction() throws Exception {
        List<Transaction> transactionList;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            try {
                transactionList = entityManager.createQuery("from Transaction", Transaction.class).getResultList();
            } catch (NoResultException e) {
                throw new TransactionException(e.getMessage());
            }
            entityManager.getTransaction().commit();
            return transactionList.stream().map(TransactionResponseDTO::fromTransaction).toList();
        } catch (TransactionException e) {
            CustomException.handleOperationException(e, entityManager);
        } catch (PersistenceException e) {
            CustomException.handlePersistenceException(e, entityManager);
        } catch (Exception e) {
            CustomException.handleUnexpectedException(e, entityManager);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
        return null;
    }

    public TransactionResponseDTO getTransactionByTransactionNumber(String transactionNumber) throws Exception {
        Transaction transaction;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            transaction = TransactionServiceUtil.findTransactionByTransactionNumber(entityManager, transactionNumber);
            entityManager.getTransaction().commit();
            return TransactionResponseDTO.fromTransaction(transaction);
        } catch (TransactionException e) {
            CustomException.handleOperationException(e, entityManager);
        } catch (PersistenceException e) {
            CustomException.handlePersistenceException(e, entityManager);
        } catch (Exception e) {
            CustomException.handleUnexpectedException(e, entityManager);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
        return null;
    }

    public List<TransactionResponseDTO> getALLTransactionByUsername(String username) throws Exception {
        User user;
        List<Account> accountList;
        List<Transaction> transactionList;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            user = UserServiceUtil.findUserByName(entityManager, username);
            accountList = AccountServiceUtil.findAccountsByUser(entityManager, user);
            transactionList = TransactionServiceUtil.findTransactionsForAccounts(entityManager, accountList);

            entityManager.getTransaction().commit();
            return transactionList.stream().map(TransactionResponseDTO::fromTransaction).toList();
        } catch (TransactionException | AccountNotFoundException | UserNotFoundException e) {
            CustomException.handleOperationException(e, entityManager);
        } catch (PersistenceException e) {
            CustomException.handlePersistenceException(e, entityManager);
        } catch (Exception e) {
            CustomException.handleUnexpectedException(e, entityManager);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
        return null;
    }

    public List<TransactionResponseDTO> getAllTransactionByAccountNumber(String accountNumber) throws Exception {
        Account account;
        List<Transaction> transactionList;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            account = AccountServiceUtil.findAccountByAccountNumber(entityManager, accountNumber, "Account not found");
            transactionList = TransactionServiceUtil.findTransactions(entityManager, account);
            entityManager.getTransaction().commit();

            return transactionList.stream().map(TransactionResponseDTO::fromTransaction).toList();
        } catch (TransactionException | AccountNotFoundException e) {
            CustomException.handleOperationException(e, entityManager);
        } catch (PersistenceException e) {
            CustomException.handlePersistenceException(e, entityManager);
        } catch (Exception e) {
            CustomException.handleUnexpectedException(e, entityManager);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
        return null;
    }

    public TransactionResponseDTO createDepositTransaction(TransactionRequestDTO transactionRequestDTO) throws TransactionException, AccountNotFoundException {
        Account account;
        Account systemAccount;
        Transaction transaction;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            String accountNumber = transactionRequestDTO.accountNumber();
            Double amount = transactionRequestDTO.amount();

            account = AccountServiceUtil.findAccountByAccountNumber(entityManager, accountNumber, "To Account not found");

            systemAccount = AccountServiceUtil.getSystemAccount();

            transaction = TransactionServiceUtil.createTransaction(systemAccount, account, amount);

            entityManager.persist(transaction);
            entityManager.persist(account);
            entityManager.getTransaction().commit();

            return TransactionResponseDTO.fromTransaction(transaction);
        } catch (TransactionException | AccountNotFoundException e) {
            CustomException.handleOperationException(e, entityManager);
        } catch (PersistenceException e) {
            CustomException.handlePersistenceException(e, entityManager);
        } catch (Exception e) {
            CustomException.handleUnexpectedException(e, entityManager);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
        return null;
    }

    public TransferResponseDTO createTransferTransaction(TransferRequestDTO transferRequestDTO) throws Exception {
        Account fromAccount;
        Account toAccount;
        Transaction transaction;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            String fromAccountStr = transferRequestDTO.fromAccount();
            String toAccountStr = transferRequestDTO.toAccount();
            Double amount = transferRequestDTO.amount();

            fromAccount = AccountServiceUtil.findAccountByAccountNumber(entityManager, fromAccountStr, "From Account not found");
            toAccount = AccountServiceUtil.findAccountByAccountNumber(entityManager, toAccountStr, "To Account not found");

            AccountServiceUtil.validateAccounts(fromAccount, toAccount, amount);

            transaction = TransactionServiceUtil.createTransaction(fromAccount, toAccount, amount);

            entityManager.persist(transaction);
            entityManager.persist(fromAccount);
            entityManager.persist(toAccount);
            entityManager.getTransaction().commit();

            return TransferResponseDTO.fromTransfer(transaction, fromAccount, toAccount);
        } catch (TransactionException | AccountNotFoundException e) {
            CustomException.handleOperationException(e, entityManager);
        } catch (PersistenceException e) {
            CustomException.handlePersistenceException(e, entityManager);
        } catch (Exception e) {
            CustomException.handleUnexpectedException(e, entityManager);
        } finally {
            ConnectionDriver.closeConnection(entityManager);
        }
        return null;
    }


}
