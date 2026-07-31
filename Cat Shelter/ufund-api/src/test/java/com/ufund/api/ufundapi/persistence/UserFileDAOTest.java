package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;

public class UserFileDAOTest {
    private UserFileDAO userFileDAO;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real temp file with an empty JSON array
        tempFile = File.createTempFile("users_test", ".json");
        tempFile.deleteOnExit();
        new ObjectMapper().writeValue(tempFile, new User[0]);

        // Create DAO and point its filePath at the temp file via reflection
        userFileDAO = new UserFileDAO(new ObjectMapper());
        Field filePathField = UserFileDAO.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(userFileDAO, tempFile.getAbsolutePath());

        // Reset the users map so each test starts clean
        Field usersField = UserFileDAO.class.getDeclaredField("users");
        usersField.setAccessible(true);
        usersField.set(userFileDAO, new java.util.HashMap<>());
    }

    @Test
    void testCreateUser() throws IOException {
        User user = new User(0, "cat", "password123", "helper");
        User created = userFileDAO.createUser(user);

        assertNotNull(created);
        assertEquals(1, created.getId());
        assertEquals("cat", created.getName());
        assertEquals("helper", created.getRole());
    }

    @Test
    void testGetUserByIDFound() throws IOException {
        userFileDAO.createUser(new User(0, "not cat", "pass456", "helper"));
        User found = userFileDAO.getUserByID(1);

        assertNotNull(found);
        assertEquals("not cat", found.getName());
    }

    @Test
    void testGetUserByIDNotFound() throws IOException {
        User found = userFileDAO.getUserByID(99);
        assertNull(found);
    }

    @Test
    void testGetUsers() throws IOException {
        userFileDAO.createUser(new User(0, "cat", "pass1", "helper"));
        userFileDAO.createUser(new User(0, "not cat", "pass2", "admin"));

        List<User> users = userFileDAO.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testFindUsersMatch() throws IOException {
        userFileDAO.createUser(new User(0, "car1", "pass1", "helper"));
        userFileDAO.createUser(new User(0, "car2", "pass2", "helper"));
        userFileDAO.createUser(new User(0, "cat not other word", "pass3", "helper"));

        List<User> results = userFileDAO.findUsers("car");
        assertEquals(2, results.size());
    }

    @Test
    void testFindUsersNoMatch() throws IOException {
        userFileDAO.createUser(new User(0, "cat", "pass1", "helper"));

        List<User> results = userFileDAO.findUsers("xyz");
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindUsersCaseInsensitive() throws IOException {
        userFileDAO.createUser(new User(0, "cat", "pass1", "helper"));

        List<User> results = userFileDAO.findUsers("cat");
        assertEquals(1, results.size());
        assertEquals("cat", results.get(0).getName());
    }

    @Test
    void testUpdateUserSuccess() throws IOException {
        userFileDAO.createUser(new User(0, "running out of cats", "oldpass", "helper"));

        User updated = new User(1, "running out of cats", "newpass", "admin");
        User result = userFileDAO.updateUser(updated);

        assertNotNull(result);
        assertEquals("newpass", result.getPassword());
        assertEquals("admin", result.getRole());
    }

    @Test
    void testUpdateUserNotFound() throws IOException {
        User updated = new User(99, "ghost", "pass", "helper");
        User result = userFileDAO.updateUser(updated);
        assertNull(result);
    }

    @Test
    void testDeleteUserSuccess() throws IOException {
        userFileDAO.createUser(new User(0, "deleted cat", "pass", "helper"));

        boolean deleted = userFileDAO.deleteUser(1);
        assertTrue(deleted);
        assertNull(userFileDAO.getUserByID(1));
    }

    @Test
    void testDeleteUserNotFound() throws IOException {
        boolean deleted = userFileDAO.deleteUser(99);
        assertFalse(deleted);
    }
}