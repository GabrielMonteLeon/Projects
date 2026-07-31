package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;

@Component
public class UserFileDAO implements UserDAO {

    private Map<Integer, User> users = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final String filePath = "data/users.json";

    //initializes objectMapper and triggers load
    public UserFileDAO(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        load();
    }

    // Load needs from file on startup
    private void load() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            save();
            return;
        }
        User[] loadedUsers = objectMapper.readValue(file, User[].class);
        for (User user : loadedUsers) {
            users.put(user.getId(), user);
        }
    }

    // Save current state to file after every change, if there is nothing writes an empty array
    private void save() throws IOException {
        objectMapper.writeValue(new File(filePath), users.values());
    }

    //gets all users
    @Override
    public List<User> getUsers() throws IOException{
        return new ArrayList<>(users.values());
    }

    //gets the user by their id
    @Override
    public User getUserByID(int id) throws IOException{
        return users.get(id);
    }

    //finds all users with the name from search text
    @Override
    public List<User> findUsers(String searchText) throws IOException{
        List<User> results = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getName().toLowerCase().contains(searchText.toLowerCase())) {
                results.add(user);
            }
        }        
        return results;    
    }

    //creates a new user on the map
    @Override
    public User createUser(User user) throws IOException{
        user.setId(users.size() + 1);
        users.put(user.getId(), user);
        save();
        return user;    
    }

    //updates the given user
    @Override
    public User updateUser(User user) throws IOException{
        if(users.containsKey(user.getId())){
            users.put(user.getId(), user);
            save();
            return user;
        }
        return null;
    }

    //Deletes the user with the given id
    @Override
    public boolean deleteUser(int id) throws IOException{
        if(!users.containsKey(id)) return false;
        users.remove(id);
        return true;
    }
}
