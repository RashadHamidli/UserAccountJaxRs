package com.company.service;

import com.company.dto.request.AccountCreateRequestDTO;
import com.company.dto.request.AccountUpdateRequestDTO;
import com.company.dto.response.AccountResponseDTO;
import com.company.entity.Account;
import com.company.entity.User;
import com.company.exeption.AccountNotFoundException;
import com.company.exeption.CustomException;
import com.company.exeption.UserNotFoundException;
import com.company.util.AccountServiceUtil;
import com.company.util.ConnectionDriver;
import com.company.util.UserServiceUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AccountService {

    private EntityManager entityManager;

    public AccountResponseDTO createAccount(AccountCreateRequestDTO accountCreateRequestDTO) throws Exception {
        Account account = AccountCreateRequestDTO.toAccount(accountCreateRequestDTO);
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            User user = UserServiceUtil.findUserByName(entityManager, accountCreateRequestDTO.username());
            account.setUser(user);
            AccountServiceUtil.saveAccount(account);

            entityManager.persist(account);
            entityManager.getTransaction().commit();
            return AccountResponseDTO.fromAccount(account);
        } catch (UserNotFoundException e) {
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

    public AccountResponseDTO updateAccountByAccountNumber(AccountUpdateRequestDTO accountUpdateRequestDTO) throws Exception {

        Account foundAccount;
        Account accountRequest = AccountUpdateRequestDTO.toAccount(accountUpdateRequestDTO);
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            foundAccount = AccountServiceUtil.findAccountByAccountNumber(entityManager, accountRequest.getAccountNumber(), "Account not found");
            AccountServiceUtil.updateAccount(foundAccount, accountRequest);

            entityManager.merge(foundAccount);
            entityManager.getTransaction().commit();
            return AccountResponseDTO.fromAccount(foundAccount);
        } catch (AccountNotFoundException e) {
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


    public List<AccountResponseDTO> getAllAccount() throws Exception {
        List<Account> accounts;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            accounts = AccountServiceUtil.findAccounts(entityManager, true);
            entityManager.getTransaction().commit();

            return accounts.stream().map(AccountResponseDTO::fromAccount).toList();
        } catch (AccountNotFoundException e) {
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


    public AccountResponseDTO getAccountByAccountNumber(String accountNumber) throws Exception {
        Account account;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            account = AccountServiceUtil.findAccountByAccountNumber(entityManager, accountNumber, "Account Not Found");

            entityManager.getTransaction().commit();
            return AccountResponseDTO.fromAccount(account);
        } catch (AccountNotFoundException e) {
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


    public AccountResponseDTO deleteAccountByAccountNumber(String accountNumber) throws Exception {
        Account account;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            account = AccountServiceUtil.findAccountByAccountNumber(entityManager, accountNumber, "Account Not Found");
            entityManager.remove(account);

            entityManager.getTransaction().commit();
            return AccountResponseDTO.fromAccount(account);
        } catch (AccountNotFoundException e) {
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
