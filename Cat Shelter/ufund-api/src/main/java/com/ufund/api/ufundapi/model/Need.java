package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Need {
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("quantity")private int quantity;
    @JsonProperty("type")private String type;
    @JsonProperty("cost")private float cost;
    @JsonProperty("userId") private int userId;

    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("quantity") int quantity, @JsonProperty("type") String type, @JsonProperty("cost") float cost, @JsonProperty("userId") int userId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.cost = cost;
        this.userId = userId;
    }

    //gets the id of the need
    public int getId() {
        return id;
    }

    //sets the id of the need
    public int setId(int id) {
        this.id = id;
        return this.id;
    }

    //gets the name of the need
    public String getName() {
        return name;
    }

    //gets the quantity of the need
    public int getQuantity() {
        return quantity;
    }

    //gets the type of the need
    public String getType() {
        return type;
    }

    //gets the cost of the need
    public float getCost() {
        return cost;
    }

    //gets the user id
    public int getUserId() {
        return userId;
    }
}

