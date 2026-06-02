package com.library.models;

public abstract class User
{
    private String userId;
    private String name;

    // parent class constructor
    User (String userId, String name)
    {
        this.userId = userId;
        this.name = name;
    }

    // abstract method
    public abstract String getRole();

    // getters
    public String getUserId() {return userId;}
    public String getName() {return name;}
}