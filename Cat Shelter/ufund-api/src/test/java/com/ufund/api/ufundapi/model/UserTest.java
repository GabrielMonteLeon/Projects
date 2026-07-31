package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class UserTest {

    @Test
    public void testGetName() {
        // Tests the getName method of the User class to ensure it returns the correct name
        User user = new User(1, "cat", "pass123", "helper");
        assertEquals("cat", user.getName());
    }

    @Test
    public void testGetPassword() {
        // Tests the getPassword method of the User class to ensure it returns the correct password
        User user = new User(1, "cat", "pass123", "helper");
        assertEquals("pass123", user.getPassword());
    }

    @Test
    public void testGetRole() {
        // Tests the getRole method of the User class to ensure it returns the correct role
        User user = new User(1, "cat", "pass123", "helper");
        assertEquals("helper", user.getRole());
    }

    @Test
    public void testSetId() {
        // Tests the setId method of the User class to ensure it correctly sets the ID
        User user = new User(1, "cat", "pass123", "helper");
        user.setId(42);
        assertEquals(42, user.getId());
    }

    @Test
    public void testSetPassword() {
        // Tests the setPassword method of the User class to ensure it correctly updates the password
        User user = new User(1, "cat", "oldpass", "helper");
        user.setPassword("newpass");
        assertEquals("newpass", user.getPassword());
    }

    @Test
    public void testSetRole() {
        // Tests the setRole method of the User class to ensure it correctly updates the role
        User user = new User(1, "cat", "pass123", "helper");
        user.setRole("admin");
        assertEquals("admin", user.getRole());
    }
}