package com.ufund.api.ufundapi.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.persistence.CartDAO;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartDAO cartDAO;

    // sets the cartDAO
    public CartController(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    // gets the cart for a user
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable int userId) {
        try {
            Cart cart = cartDAO.getCart(userId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // adds a need to the cart
    @PostMapping("/{userId}/add/{needId}")
    public ResponseEntity<Cart> addNeed(@PathVariable int userId, @PathVariable int needId) {
        try {
            Cart cart = cartDAO.addNeedToCart(userId, needId);
            if (cart == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // removes a need from the cart
    @DeleteMapping("/{userId}/remove/{needId}")
    public ResponseEntity<Cart> removeNeed(@PathVariable int userId, @PathVariable int needId) {
        try {
            Cart cart = cartDAO.removeNeedFromCart(userId, needId);
            if (cart == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // checks out the cart and returns the total cost
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Float> checkout(@PathVariable int userId) {
        try {
            float total = cartDAO.checkout(userId);
            return new ResponseEntity<>(total, HttpStatus.OK);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}