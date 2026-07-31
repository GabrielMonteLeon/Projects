package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.model.Need;

public class CartDAOTest {

    private static final String DATA_FILE = "data/carts.json";

    private CartFileDAO cartFileDAO;
    private NeedDAO mockNeedDAO;
    private ObjectMapper objectMapper;

    private Need makeNeed(int id, String name, int qty, float cost) {
        return new Need(id, name, qty, "Supply", cost, 1);
    }

    /** Writes carts to data/carts.json then constructs a fresh DAO. */
    private CartFileDAO daoFromCarts(Cart[] carts) throws IOException {
        File file = new File(DATA_FILE);
        file.getParentFile().mkdirs();
        objectMapper.writeValue(file, carts);
        return new CartFileDAO(objectMapper, mockNeedDAO);
    }

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        mockNeedDAO = mock(NeedDAO.class);
        cartFileDAO = daoFromCarts(new Cart[0]);
    }

    @AfterEach
    void tearDown() {
        new File(DATA_FILE).delete();
    }

    @Test
    void testLoad_FileDoesNotExist_CreatesFileAndSaves() throws IOException {
        new File(DATA_FILE).delete();

        CartFileDAO dao = new CartFileDAO(objectMapper, mockNeedDAO);

        assertTrue(new File(DATA_FILE).exists());
        assertTrue(dao.getCart(1).getNeedIds().isEmpty());
    }

    @Test
    void testLoad_EmptyFile_ReturnsWithoutParsing() throws IOException {
        // Truncate data/carts.json to 0 bytes
        File file = new File(DATA_FILE);
        new FileOutputStream(file, false).close();

        CartFileDAO dao = new CartFileDAO(objectMapper, mockNeedDAO);

        assertTrue(dao.getCart(99).getNeedIds().isEmpty());
    }

    @Test
    void testLoad_FileWithData_PopulatesCarts() throws IOException {
        List<Integer> ids = new ArrayList<>(List.of(10));
        CartFileDAO dao = daoFromCarts(new Cart[]{ new Cart(5, ids) });

        Cart loaded = dao.getCart(5);
        assertNotNull(loaded);
        assertTrue(loaded.getNeedIds().contains(10));
    }

    @Test
    void testGetCart_NoExistingCart_ReturnsEmptyCart() throws IOException {
        Cart cart = cartFileDAO.getCart(99);
        assertNotNull(cart);
        assertEquals(99, cart.getUserId());
        assertTrue(cart.getNeedIds().isEmpty());
    }

    @Test
    void testGetCart_ExistingCart_ReturnsSameCart() throws IOException {
        cartFileDAO.getCart(1);
        Cart second = cartFileDAO.getCart(1);
        assertEquals(1, second.getUserId());
    }

    @Test
    void testAddNeedToCart_NeedNotFound_ReturnsNull() throws IOException {
        when(mockNeedDAO.getNeedByID(42)).thenReturn(null);

        assertNull(cartFileDAO.addNeedToCart(1, 42));
    }

    @Test
    void testAddNeedToCart_NeedExists_AddsToCart() throws IOException {
        when(mockNeedDAO.getNeedByID(1)).thenReturn(makeNeed(1, "Food", 5, 10f));

        Cart result = cartFileDAO.addNeedToCart(7, 1);

        assertNotNull(result);
        assertTrue(result.getNeedIds().contains(1));
    }

    @Test
    void testAddNeedToCart_AlreadyInCart_DoesNotDuplicate() throws IOException {
        when(mockNeedDAO.getNeedByID(1)).thenReturn(makeNeed(1, "Food", 5, 10f));

        cartFileDAO.addNeedToCart(7, 1);
        cartFileDAO.addNeedToCart(7, 1);

        assertEquals(1, cartFileDAO.getCart(7).getNeedIds().size());
    }

    @Test
    void testRemoveNeedFromCart_NeedNotInCart_ReturnsNull() throws IOException {
        assertNull(cartFileDAO.removeNeedFromCart(1, 999));
    }

    @Test
    void testRemoveNeedFromCart_NeedInCart_RemovesAndReturnsCart() throws IOException {
        when(mockNeedDAO.getNeedByID(2)).thenReturn(makeNeed(2, "Water", 3, 5f));

        cartFileDAO.addNeedToCart(3, 2);
        Cart result = cartFileDAO.removeNeedFromCart(3, 2);

        assertNotNull(result);
        assertFalse(result.getNeedIds().contains(2));
    }

    @Test
    void testCheckout_NeedNullInCart_Skipped() throws IOException {
        cartFileDAO.getCart(10).addNeedId(55);
        when(mockNeedDAO.getNeedByID(55)).thenReturn(null);

        float total = cartFileDAO.checkout(10);

        assertEquals(0f, total, 0.001f);
        assertTrue(cartFileDAO.getCart(10).getNeedIds().isEmpty());
    }

    @Test
    void testCheckout_QuantityBecomesZero_DeletesNeed() throws IOException {
        when(mockNeedDAO.getNeedByID(3)).thenReturn(makeNeed(3, "Blanket", 1, 20f));

        cartFileDAO.getCart(20).addNeedId(3);
        float total = cartFileDAO.checkout(20);

        assertEquals(20f, total, 0.001f);
        verify(mockNeedDAO).deleteNeed(3);
        verify(mockNeedDAO, never()).updateNeed(any());
        assertTrue(cartFileDAO.getCart(20).getNeedIds().isEmpty());
    }

    @Test
    void testCheckout_QuantityAboveOne_UpdatesNeed() throws IOException {
        when(mockNeedDAO.getNeedByID(4)).thenReturn(makeNeed(4, "Shoes", 5, 30f));

        cartFileDAO.getCart(21).addNeedId(4);
        float total = cartFileDAO.checkout(21);

        assertEquals(30f, total, 0.001f);
        verify(mockNeedDAO, never()).deleteNeed(anyInt());
        verify(mockNeedDAO).updateNeed(argThat(n -> n.getQuantity() == 4));
        assertTrue(cartFileDAO.getCart(21).getNeedIds().isEmpty());
    }

    @Test
    void testCheckout_MultipleNeeds_TotalIsSumOfCosts() throws IOException {
        when(mockNeedDAO.getNeedByID(5)).thenReturn(makeNeed(5, "Hat",  1, 15f));
        when(mockNeedDAO.getNeedByID(6)).thenReturn(makeNeed(6, "Coat", 3, 50f));

        Cart cart = cartFileDAO.getCart(22);
        cart.addNeedId(5);
        cart.addNeedId(6);

        float total = cartFileDAO.checkout(22);

        assertEquals(65f, total, 0.001f);
        verify(mockNeedDAO).deleteNeed(5);
        verify(mockNeedDAO).updateNeed(argThat(n -> n.getId() == 6 && n.getQuantity() == 2));
    }

    @Test
    void testCheckout_EmptyCart_ReturnsZero() throws IOException {
        assertEquals(0f, cartFileDAO.checkout(30), 0.001f);
    }
}