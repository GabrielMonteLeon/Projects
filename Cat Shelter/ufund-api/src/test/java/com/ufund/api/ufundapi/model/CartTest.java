package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CartTest {

    @Test
    void testConstructor_WithNeedIds_StoresBoth() {
        List<Integer> ids = new ArrayList<>(List.of(1, 2, 3));
        Cart cart = new Cart(5, ids);

        assertEquals(5, cart.getUserId());
        assertEquals(ids, cart.getNeedIds());
    }

    @Test
    void testConstructor_NullNeedIds_InitializesEmptyList() {
        Cart cart = new Cart(7, null);

        assertEquals(7, cart.getUserId());
        assertNotNull(cart.getNeedIds());
        assertTrue(cart.getNeedIds().isEmpty());
    }

    @Test
    void testGetUserId_ReturnsCorrectId() {
        Cart cart = new Cart(42, null);
        assertEquals(42, cart.getUserId());
    }

    @Test
    void testGetNeedIds_ReturnsLiveList() {
        Cart cart = new Cart(1, new ArrayList<>());
        cart.getNeedIds().add(99);
        assertTrue(cart.getNeedIds().contains(99));
    }

    @Test
    void testAddNeedId_NewId_ReturnsTrueAndAdds() {
        Cart cart = new Cart(1, new ArrayList<>());

        boolean result = cart.addNeedId(10);

        assertTrue(result);
        assertTrue(cart.getNeedIds().contains(10));
    }

    @Test
    void testAddNeedId_DuplicateId_ReturnsFalseAndDoesNotAdd() {
        Cart cart = new Cart(1, new ArrayList<>(List.of(10)));

        boolean result = cart.addNeedId(10);

        assertFalse(result);
        assertEquals(1, cart.getNeedIds().size());
    }

    @Test
    void testRemoveNeedId_PresentId_ReturnsTrueAndRemoves() {
        Cart cart = new Cart(1, new ArrayList<>(List.of(5, 6)));

        boolean result = cart.removeNeedId(5);

        assertTrue(result);
        assertFalse(cart.getNeedIds().contains(5));
        assertTrue(cart.getNeedIds().contains(6));
    }

    @Test
    void testRemoveNeedId_AbsentId_ReturnsFalse() {
        Cart cart = new Cart(1, new ArrayList<>(List.of(5)));

        boolean result = cart.removeNeedId(99);

        assertFalse(result);
        assertEquals(1, cart.getNeedIds().size());
    }
}