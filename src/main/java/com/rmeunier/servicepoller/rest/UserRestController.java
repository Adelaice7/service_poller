package com.rmeunier.servicepoller.rest;

import com.rmeunier.servicepoller.model.User;
import com.rmeunier.servicepoller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
/**
 * REST API endpoints for sending User requests.
 */
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String username) {
        List<User> users = userService.getAllUsers(username);
        if (users != null) {
            return ResponseEntity.accepted().body(users);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);

        if (user != null) {
            return ResponseEntity.accepted().body(user);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (userService.addUser(user) != null) {
            return ResponseEntity.accepted().body(user);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User userRequest) {
        User user = userService.updateUser(id, userRequest);

        if (user != null) {
            return ResponseEntity.accepted().body(user);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") long id) {
        return userService.deleteUser(id);
    }


    @DeleteMapping("/deleteAll")
    public boolean deleteAllUsers() {
        return userService.deleteAllUsers();
    }
}
