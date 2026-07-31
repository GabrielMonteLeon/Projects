package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.model.Need;

@Component
public class CartFileDAO implements CartDAO {

    private Map<Integer, Cart> carts = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final String filePath = "data/carts.json";
    private final NeedDAO needDAO;

    // initializes objectMapper and needDAO then loads persisted carts
    public CartFileDAO(ObjectMapper objectMapper, NeedDAO needDAO) throws IOException {
        this.objectMapper = objectMapper;
        this.needDAO = needDAO;
        load();
    }

    // loads carts from file on startup
    private void load() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            save();
            return;
        }
        if (file.length() == 0) return;

        Cart[] loadedCarts = objectMapper.readValue(file, Cart[].class);
        for (Cart cart : loadedCarts) {
            carts.put(cart.getUserId(), cart);
        }
    }

    // saves current state of all carts to file
    private void save() throws IOException {
        objectMapper.writeValue(new File(filePath), carts.values());
    }

    // returns the cart for the given user, creating an empty one if none exists
    private Cart getOrCreateCart(int userId) {
        return carts.computeIfAbsent(userId, id -> new Cart(id, new ArrayList<>()));
    }

    // gets the cart
    @Override
    public Cart getCart(int userId) throws IOException {
        return getOrCreateCart(userId);
    }

    // adds a need to the user's cart and returns null if the need does not exist
    @Override
    public Cart addNeedToCart(int userId, int needId) throws IOException {
        Need need = needDAO.getNeedByID(needId);
        if (need == null) return null;

        Cart cart = getOrCreateCart(userId);
        cart.addNeedId(needId);
        save();
        return cart;
    }

    // removes a need from the user's cart and returns null if the need was not in the cart
    @Override
    public Cart removeNeedFromCart(int userId, int needId) throws IOException {
        Cart cart = getOrCreateCart(userId);
        boolean removed = cart.removeNeedId(needId);
        if (!removed) return null;
        save();
        return cart;
    }

    // checks out the user's cart, lowers quantities by 1, deletes needs that hit 0, returns the total cost of all purchased needs
    @Override
    public float checkout(int userId) throws IOException {
        Cart cart = getOrCreateCart(userId);
        float total = 0f;

        for (int needId : new ArrayList<>(cart.getNeedIds())) {
            Need need = needDAO.getNeedByID(needId);
            if (need == null) continue;

            total += need.getCost();

            int newQuantity = need.getQuantity() - 1;
            if (newQuantity <= 0) {
                needDAO.deleteNeed(needId);
                removeNeedFromAllCarts(needId);
            } 
            else {
                Need updated = new Need(
                    need.getId(),
                    need.getName(),
                    newQuantity,
                    need.getType(),
                    need.getCost(),
                    need.getUserId()
                );
                needDAO.updateNeed(updated);
            }
        }

        cart.getNeedIds().clear();
        save();
        return total;
    }

    // removes a needId from every cart that contains it if the need is deleted
    private void removeNeedFromAllCarts(int needId) throws IOException {
        for (Cart cart : carts.values()) {
            cart.removeNeedId(needId);
        }
        save();
    }

}