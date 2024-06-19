package com.company.util;

import com.company.entity.User;
import com.company.exeption.AccountNotFoundException;
import com.company.exeption.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserServiceUtil {
    public static List<User> findUsers(EntityManager entityManager) throws Exception {
        try {
            return entityManager.createQuery("from User", User.class).getResultList();
        } catch (NoResultException e) {
            throw new AccountNotFoundException("user not found");
        }
    }

    public static User findUserByName(EntityManager entityManager, String username) {
        try {
            return entityManager.createQuery("from User u where u.username=:username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("User not found!");
        }
    }

    public static User findUserById(EntityManager entityManager, Long id) {
        try {
            return entityManager.find(User.class, id);
        } catch (NoResultException e) {
            throw new UserNotFoundException("User not found!");
        }
    }

    public static User findUserByUsername(EntityManager entityManager, String username) {
        try {
            return entityManager.createQuery("from User u where u.username=:username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("User not found!");
        }
    }

    public static User saveUser(User user) {
        try {
            user.setCreateAt(LocalDateTime.now());
            return user;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found!");
        }
    }

    public static User updateUser(User foundUser, User userRequest) {
        Optional.ofNullable(userRequest.getUsername()).ifPresent(foundUser::setUsername);
        Optional.ofNullable(userRequest.getName()).ifPresent(foundUser::setName);
        Optional.ofNullable(userRequest.getSurname()).ifPresent(foundUser::setSurname);
        Optional.ofNullable(userRequest.getEmail()).ifPresent(foundUser::setEmail);
        foundUser.setUpdateAt(LocalDateTime.now());
        return foundUser;
    }
}
