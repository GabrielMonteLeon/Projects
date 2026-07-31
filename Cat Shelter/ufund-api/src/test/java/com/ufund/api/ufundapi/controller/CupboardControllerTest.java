package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.CupboardDAO;
import java.io.IOException;
import java.util.List;
import java.io.File;

@Tag("Controller-tier")
public class CupboardControllerTest {

    private CupboardController controller;

    @BeforeEach
    public void setup() throws IOException{
        // Makes sure the cupboard.json file exists and is empty
        File file = new File("data/cupboard.json");
        file.getParentFile().mkdirs();
        new ObjectMapper().writeValue(file, new Need[0]);

        // Creates ObjectMapper and DAO for the controller
        ObjectMapper objectMapper = new ObjectMapper();
        CupboardDAO dao = new CupboardDAO(objectMapper);

        // Initializes the controller with the DAO
        controller = new CupboardController(dao);
    }

    @Test
    public void testGetAllNeedsEmpty() {
        // Tests getting all needs when none exist
        ResponseEntity<Need[]> response = controller.getAllNeeds();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetNeedNotFound() {
        // Tries to get a need that doesn't exist
        ResponseEntity<Need> response = controller.getNeed(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //successfully gets a need
    @Test
    public void testGetNeedSuccess() {
        Need need = new Need(1, "Food", 10, "supplies", 5.99f, 0);
        controller.createNeed(need);

        ResponseEntity<Need> response = controller.getNeed(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    public void testCreateNeed() {
        // Creates a new need
        Need need = new Need(1, "Food", 10, "supplies", 5.99f, 0);
        ResponseEntity<Need> response = controller.createNeed(need);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Tries to delete a need that doesn't exist
        ResponseEntity<Void> response = controller.deleteNeed(99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //successfully deletes aneed
    @Test
    public void testDeleteNeedSuccess() {
        Need need = new Need(1, "Food", 10, "supplies", 5.99f, 0);
        controller.createNeed(need);

        ResponseEntity<Void> response = controller.deleteNeed(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    public void testUpdateNeedNotFound() {
        // Tries to update a need that doesn't exist
        Need need = new Need(99, "Ghost", 1, "none", 0.0f, 0);
        ResponseEntity<Need> response = controller.updateNeed(need);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //successfully updates a need
    @Test
    public void testUpdateNeedSuccess() {
        Need need = new Need(1, "Food", 10, "supplies", 5.99f, 0);
        controller.createNeed(need);

        Need updated = new Need(1, "Food", 20, "supplies", 5.99f, 0);
        ResponseEntity<Need> response = controller.updateNeed(updated);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(20, response.getBody().getQuantity());
    }

    //gets all needs when its not empty
    @Test
    public void testGetAllNeedsNonEmpty() {
        controller.createNeed(new Need(1, "Food", 10, "supplies", 5.99f, 0));

        ResponseEntity<Need[]> response = controller.getAllNeeds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    //tests search needs
    @Test
    public void testSearchNeeds() {
        controller.createNeed(new Need(1, "Food", 10, "supplies", 5.99f, 0));
        controller.createNeed(new Need(2, "Water", 5, "supplies", 2.99f, 0));

        List<Need> results = controller.searchNeeds("Food");

        assertEquals(1, results.size());
        assertEquals("Food", results.get(0).getName());
    }

    //tests gets need by user
    @Test
    public void testGetNeedsByUser() {
        controller.createNeed(new Need(1, "Food", 10, "supplies", 5.99f, 1));
        controller.createNeed(new Need(2, "Water", 5, "supplies", 2.99f, 2));

        ResponseEntity<Need[]> response = controller.getNeedsByUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }
}