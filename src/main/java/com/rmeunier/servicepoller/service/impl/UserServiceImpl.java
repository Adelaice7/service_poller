package com.rmeunier.servicepoller.service.impl;

import com.rmeunier.servicepoller.model.User;
import com.rmeunier.servicepoller.repo.UserRepository;
import com.rmeunier.servicepoller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/**
 * This service is responsible for communicating with the repository of the User objects.
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers(String username) {
        List<User> users = new ArrayList<>();
        if (username == null) {
            users.addAll(userRepository.findAll());
        } else {
            users.add(userRepository.findByUsername(username));
        }

        if (users.isEmpty()) {
            return null;
        }
        return users;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setPassword(updatedUser.getPassword());
            return userRepository.save(user);
        }).orElse(null);
    }

    @Override
    public boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteAllUsers() {
        userRepository.deleteAll();
        return true;
    }
}
