package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

public class CupboardDAOTest {
    private CupboardDAO cupboardDAO;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real temp file with an empty JSON array
        tempFile = File.createTempFile("cupboard_test", ".json");
        tempFile.deleteOnExit();
        new ObjectMapper().writeValue(tempFile, new Need[0]);

        // Create DAO and point its filePath at the temp file via reflection
        cupboardDAO = new CupboardDAO(new ObjectMapper());
        Field filePathField = CupboardDAO.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(cupboardDAO, tempFile.getAbsolutePath());

        // Reload using the temp file path now set
        Field needsField = CupboardDAO.class.getDeclaredField("needs");
        needsField.setAccessible(true);
        needsField.set(cupboardDAO, new java.util.HashMap<>());

        Field nextIdField = CupboardDAO.class.getDeclaredField("nextId");
        nextIdField.setAccessible(true);
        nextIdField.set(cupboardDAO, 1);
    }

    @Test
    void testCreateNeed() throws IOException {
        // Makes a new need
        Need need = new Need(0, "Food", 10, "Supply", 5.0f,0);

        // Adds it to the DAO
        Need created = cupboardDAO.createNeed(need);

        // Checks if it was added correctly
        assertNotNull(created);
        assertEquals(1, created.getId());
        assertEquals("Food", created.getName());
    }

    @Test
    void testGetNeedByIDFound() throws IOException {
        // Adds a need
        Need need = new Need(0, "Water", 5, "Supply", 3.0f,0);
        cupboardDAO.createNeed(need);

        // Gets it by ID
        Need found = cupboardDAO.getNeedByID(1);

        // Checks if it is returned
        assertNotNull(found);
        assertEquals("Water", found.getName());
    }

    @Test
    void testGetNeedByIDNotFound() throws IOException {
        // Tries to get a need that doesn't exist
        Need found = cupboardDAO.getNeedByID(99);

        // Should return null
        assertNull(found);
    }

    @Test
    void testFindNeedsMatch() throws IOException {
        // Adds a few needs
        cupboardDAO.createNeed(new Need(0, "Blanket", 2, "Item", 10f,0));
        cupboardDAO.createNeed(new Need(0, "Food Box", 3, "Item", 15f,0));

        // Searches for "food"
        List<Need> results = cupboardDAO.findNeeds("food");

        // Should find only the matching one
        assertEquals(1, results.size());
        assertEquals("Food Box", results.get(0).getName());
    }

    @Test
    void testFindNeedsNoMatch() throws IOException {
        // Adds a need
        cupboardDAO.createNeed(new Need(0, "Tent", 1, "Item", 50f,0));

        // Searches for something that doesn't exist
        List<Need> results = cupboardDAO.findNeeds("water");

        // Should return empty list
        assertTrue(results.isEmpty());
    }

    @Test
    void testUpdateNeedSuccess() throws IOException {
        // Adds a need
        Need need = new Need(0, "Shoes", 4, "Clothing", 20f,0);
        cupboardDAO.createNeed(need);

        // Updates the quantity
        Need updated = new Need(1, "Shoes", 10, "Clothing", 20f,0);
        Need result = cupboardDAO.updateNeed(updated);

        // Should be updated
        assertNotNull(result);
        assertEquals(10, result.getQuantity());
    }

    @Test
    void testUpdateNeedNotFound() throws IOException {
        // Tries to update a need that doesn't exist
        Need updated = new Need(99, "Fake", 1, "None", 0f,0);

        Need result = cupboardDAO.updateNeed(updated);

        // Should return null
        assertNull(result);
    }

    @Test
    void testDeleteNeedSuccess() throws IOException {
        // Adds a need
        cupboardDAO.createNeed(new Need(0, "Jacket", 1, "Clothing", 30f,0));

        // Deletes it
        boolean deleted = cupboardDAO.deleteNeed(1);

        // Should be true
        assertTrue(deleted);
    }

    @Test
    void testDeleteNeedNotFound() throws IOException {
        // Tries to delete something that doesn't exist
        boolean deleted = cupboardDAO.deleteNeed(99);

        // Should be false
        assertFalse(deleted);
    }
}