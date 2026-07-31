package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserDAO;

public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        // Mock the DAO so no filesystem is touched
        mockUserDAO = Mockito.mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    void testRegisterSuccess() throws Exception {
        User newUser = new User(0, "probably cat", "pass123", "helper");
        User savedUser = new User(1, "probably cat", "pass123", "helper");

        when(mockUserDAO.findUsers("probably cat")).thenReturn(Collections.emptyList());
        when(mockUserDAO.createUser(newUser)).thenReturn(savedUser);

        ResponseEntity<User> response = userController.createUser(newUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("probably cat", response.getBody().getName());
    }

    @Test
    void testRegisterConflict() throws Exception {
        User existingUser = new User(1, "probably cat", "pass123", "helper");
        User newUser = new User(0, "probably cat", "different", "helper");

        when(mockUserDAO.findUsers("probably cat")).thenReturn(List.of(existingUser));

        ResponseEntity<User> response = userController.createUser(newUser);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testRegisterInternalServerError() throws Exception {
        User newUser = new User(0, "probably cat", "pass123", "helper");

        when(mockUserDAO.findUsers("probably cat")).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<User> response = userController.createUser(newUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testLoginSuccess() throws Exception {
        User loginRequest = new User(0, "cat", "correctpass", "helper");
        User storedUser = new User(2, "cat", "correctpass", "helper");

        when(mockUserDAO.findUsers("cat")).thenReturn(List.of(storedUser));

        ResponseEntity<User> response = userController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getId());
        assertEquals("cat", response.getBody().getName());
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        User loginRequest = new User(0, "nobody", "pass", "helper");

        when(mockUserDAO.findUsers("nobody")).thenReturn(Collections.emptyList());

        ResponseEntity<User> response = userController.login(loginRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        User loginRequest = new User(0, "not cat", "wrongpass", "helper");
        User storedUser = new User(3, "not cat", "rightpass", "helper");

        when(mockUserDAO.findUsers("not cat")).thenReturn(List.of(storedUser));

        ResponseEntity<User> response = userController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLoginInternalServerError() throws Exception {
        User loginRequest = new User(0, "maybe cat", "pass", "helper");

        when(mockUserDAO.findUsers("maybe cat")).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<User> response = userController.login(loginRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}