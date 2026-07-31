package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("password")private String password;
    @JsonProperty("role")private String role;

    //Sets all the initial values of the User
    public User(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty("role") String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    //gets the id of the user
    public int getId() {
        return id;
    }

    //changes the id of the user
    public int setId(int id) {
        this.id = id;
        return this.id;
    }

    //gets the name of the user
    public String getName() {
        return name;
    }

    //gets the password for the user
    public String getPassword() {
        return password;
    }

    //Changes the password of the user
    public String setPassword(String newPassword) {
        password=newPassword;
        return password;
    }

    //gets the password for the user
    public String getRole() {
        return role;
    }

    //Changes the password of the user
    public String setRole(String newRole) {
        role=newRole;
        return role;
    }
}

