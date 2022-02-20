package com.rmeunier.servicepoller.service;

import com.rmeunier.servicepoller.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(String username);
    User getUserById(Long id);
    User addUser(User user);
    User updateUser(Long id, User updatedUser);
    boolean deleteUser(Long id);
    boolean deleteAllUsers();
}
