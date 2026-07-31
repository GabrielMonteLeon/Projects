package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.User;

public interface UserDAO {
    List<User> getUsers() throws IOException;
    User getUserByID(int id) throws IOException;
    List<User> findUsers(String searchText) throws IOException;
    User createUser(User user) throws IOException;
    User updateUser(User user) throws IOException;
    boolean deleteUser(int id) throws IOException;
}
