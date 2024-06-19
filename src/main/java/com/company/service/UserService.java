package com.company.service;

import com.company.dto.request.UserRequestDTO;
import com.company.dto.response.UserResponseDTO;
import com.company.entity.User;
import com.company.exeption.CustomException;
import com.company.exeption.UserNotFoundException;
import com.company.util.ConnectionDriver;
import com.company.util.UserServiceUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.List;


public class UserService {

    private EntityManager entityManager;

    public List<UserResponseDTO> getAllUsers() throws Exception {
        List<User> users;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            users = UserServiceUtil.findUsers(entityManager);
            entityManager.getTransaction().commit();
            return users.stream().map(UserResponseDTO::fromUser).toList();
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


    public UserResponseDTO getUsersById(Long id) throws Exception {
        User user;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            user = UserServiceUtil.findUserById(entityManager, id);
            return UserResponseDTO.fromUser(user);

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

    public UserResponseDTO getUserByUsername(String username) throws Exception {
        User user;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            user = UserServiceUtil.findUserByUsername(entityManager, username);
            return UserResponseDTO.fromUser(user);
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

    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) throws Exception {
        User user;
        try {
            user = userRequestDTO.toUser(userRequestDTO);
            UserServiceUtil.saveUser(user);
            return UserResponseDTO.fromUser(user);
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

    public UserResponseDTO updateUser(String username, UserRequestDTO userRequestDTO) throws Exception {
        User foundUser;
        User userRequest = UserRequestDTO.toUser(userRequestDTO);
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();
            foundUser = UserServiceUtil.findUserByName(entityManager, username);
            UserServiceUtil.updateUser(foundUser, userRequest);
            entityManager.persist(foundUser);
            entityManager.getTransaction().commit();
            return UserResponseDTO.fromUser(foundUser);
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

    public UserResponseDTO deleteUser(String username) throws Exception {
        User user;
        try {
            entityManager = ConnectionDriver.getConnection();
            entityManager.getTransaction().begin();

            user = UserServiceUtil.findUserByName(entityManager, username);
            entityManager.remove(user);
            entityManager.getTransaction().commit();
            return UserResponseDTO.fromUser(user);

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
}
