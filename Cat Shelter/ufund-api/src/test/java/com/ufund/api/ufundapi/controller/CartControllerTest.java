package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.persistence.CartDAO;

public class CartControllerTest {

    private CartDAO mockCartDAO;
    private CartController cartController;

    @BeforeEach
    void setUp() {
        mockCartDAO = mock(CartDAO.class);
        cartController = new CartController(mockCartDAO);
    }

    @Test
    void testGetCart_Success_ReturnsOk() throws IOException {
        Cart cart = new Cart(1, new ArrayList<>());
        when(mockCartDAO.getCart(1)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.getCart(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    void testGetCart_IOException_ReturnsInternalServerError() throws IOException {
        when(mockCartDAO.getCart(1)).thenThrow(new IOException());

        ResponseEntity<Cart> response = cartController.getCart(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAddNeed_Success_ReturnsOk() throws IOException {
        Cart cart = new Cart(1, new ArrayList<>());
        when(mockCartDAO.addNeedToCart(1, 10)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.addNeed(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    void testAddNeed_NeedNotFound_ReturnsNotFound() throws IOException {
        when(mockCartDAO.addNeedToCart(1, 99)).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addNeed(1, 99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddNeed_IOException_ReturnsInternalServerError() throws IOException {
        when(mockCartDAO.addNeedToCart(1, 10)).thenThrow(new IOException());

        ResponseEntity<Cart> response = cartController.addNeed(1, 10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRemoveNeed_Success_ReturnsOk() throws IOException {
        Cart cart = new Cart(1, new ArrayList<>());
        when(mockCartDAO.removeNeedFromCart(1, 10)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.removeNeed(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    void testRemoveNeed_NeedNotInCart_ReturnsNotFound() throws IOException {
        when(mockCartDAO.removeNeedFromCart(1, 99)).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeNeed(1, 99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRemoveNeed_IOException_ReturnsInternalServerError() throws IOException {
        when(mockCartDAO.removeNeedFromCart(1, 10)).thenThrow(new IOException());

        ResponseEntity<Cart> response = cartController.removeNeed(1, 10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testCheckout_Success_ReturnsTotalAndOk() throws IOException {
        when(mockCartDAO.checkout(1)).thenReturn(75.0f);

        ResponseEntity<Float> response = cartController.checkout(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(75.0f, response.getBody(), 0.001f);
    }

    @Test
    void testCheckout_IOException_ReturnsInternalServerError() throws IOException {
        when(mockCartDAO.checkout(1)).thenThrow(new IOException());

        ResponseEntity<Float> response = cartController.checkout(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}