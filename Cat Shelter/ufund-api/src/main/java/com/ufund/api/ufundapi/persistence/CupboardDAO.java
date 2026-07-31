package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

@Component
public class CupboardDAO implements NeedDAO {

    private Map<Integer, Need> needs = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final String filePath = "data/cupboard.json";
    private int nextId = 1;

    //initializes objectMapper and triggers load
    public CupboardDAO(ObjectMapper objectMapper) throws IOException {
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
        
        if (file.length() == 0) {
            return;
        }

        Need[] loadedNeeds = objectMapper.readValue(file, Need[].class);
        for (Need need : loadedNeeds) {
            needs.put(need.getId(), need);
            if (need.getId() >= nextId) {
                nextId = need.getId() + 1;
            }
        }
    }

    // Save current state to file after every change, if there is nothing writes an empty array
    private void save() throws IOException {
        objectMapper.writeValue(new File(filePath), needs.values());
    }

    //gets all needs in the map
    @Override
    public List<Need> getNeeds() throws IOException {
        return new ArrayList<>(needs.values());
    }

    //gets the need at id
    @Override
    public Need getNeedByID(int id) throws IOException {
        return needs.get(id);
    }

    //finds all needs with the name from search text
    @Override
    public List<Need> findNeeds(String searchText) throws IOException {
        List<Need> results = new ArrayList<>();
        for (Need need : needs.values()) {
            if (need.getName().toLowerCase().contains(searchText.toLowerCase())) {
                results.add(need);
            }
        }        
        return results;
    }

    //adds a new need to the map
    @Override
    public Need createNeed(Need need) throws IOException {
        need.setId(nextId++);
        needs.put(need.getId(), need);
        save();
        return need;
    }

    //returns null if the need is not placed otherwise replaces the need in the dictionary with the new updated need
    @Override
    public Need updateNeed(Need need) throws IOException {
        if (needs.containsKey(need.getId())) {
            needs.put(need.getId(), need);
            save();
            return need;
        }
        return null;
    }

    //deletes a need from the map
    @Override 
    public boolean deleteNeed(int id) throws IOException {
        if (!needs.containsKey(id)) return false;
        needs.remove(id);
        save();
        return true;
    }
    
    //gets all needs with an user id
    public List<Need> getNeedsByUser(int userId) throws IOException {
        List<Need> results = new ArrayList<>();
        for (Need need : needs.values()) {
            if (need.getUserId() == userId) {
                results.add(need);
            }
        }
        return results;
    }
}
