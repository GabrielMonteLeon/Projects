package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cart {

    @JsonProperty("userId") private int userId;
    @JsonProperty("needIds") private List<Integer> needIds;

    //initializes the cart
    public Cart(@JsonProperty("userId") int userId, @JsonProperty("needIds") List<Integer> needIds) {
        this.userId = userId;
        this.needIds = needIds != null ? needIds : new ArrayList<>();
    }

    // gets the user id this cart belongs to
    public int getUserId() {
        return userId;
    }

    // gets the list of need ids in the cart
    public List<Integer> getNeedIds() {
        return needIds;
    }

    // adds a need id to the cart, returns false if it is already present
    public boolean addNeedId(int needId) {
        if (needIds.contains(needId)) return false;
        needIds.add(needId);
        return true;
    }

    // removes a need id from the cart, returns false if it was not present
    public boolean removeNeedId(int needId) {
        return needIds.remove(Integer.valueOf(needId));
    }
}