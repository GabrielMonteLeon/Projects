package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Cart;

public interface CartDAO {
    Cart getCart(int userId) throws IOException;
    Cart addNeedToCart(int userId, int needId) throws IOException;
    Cart removeNeedFromCart(int userId, int needId) throws IOException;
    float checkout(int userId) throws IOException;
}