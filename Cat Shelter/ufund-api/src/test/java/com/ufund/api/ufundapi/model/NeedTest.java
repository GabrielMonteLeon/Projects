package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class NeedTest {

    @Test
    public void testGetName() {
        // Tests the getName method of the Need class to ensure it returns the correct name.
        Need need = new Need(1, "Food", 10, "supplies", 5.99f,0);
        assertEquals("Food", need.getName());
    }

    @Test
    public void testGetQuantity() {
        // Tests the getQuantity method of the Need class to ensure it returns the correct quantity.
        Need need = new Need(1, "Food", 10, "supplies", 5.99f,0);
        assertEquals(10, need.getQuantity());
    }

    @Test
    public void testGetCost() {
        // Tests the getCost method of the Need class to ensure it returns the correct cost.
        Need need = new Need(1, "Food", 10, "supplies", 5.99f,0);
        assertEquals(5.99f, need.getCost());
    }

    @Test
    public void testGetType() {
        // Tests the getType method of the Need class to ensure it returns the correct type.
        Need need = new Need(1, "Food", 10, "supplies", 5.99f,0);
        assertEquals("supplies", need.getType());
    }

    @Test
    public void testSetId() {
        // Tests the setId method of the Need class to ensure it correctly sets the ID.
        Need need = new Need(1, "Food", 10, "supplies", 5.99f,0);
        need.setId(42);
        assertEquals(42, need.getId());
    }
}