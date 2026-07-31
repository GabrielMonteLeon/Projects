package com.ufund.api.ufundapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserDAO;



@RestController
@RequestMapping("/users")
public class UserController {

    private UserDAO userDAO;

    //sets the UserController userDAO
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    //creates a new user
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            List<User> existing = userDAO.findUsers(user.getName());
            if (!existing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(userDAO.createUser(user), HttpStatus.OK);
        } 
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //logs the user in
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        try {
            List<User> found = userDAO.findUsers(user.getName());
            if (found.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            User match = found.get(0);
            if (!match.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(match, HttpStatus.OK);
        } 
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
