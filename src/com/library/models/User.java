package com.library.models;

public abstract class User
{
    protected String userId;
    protected String name;

    User (String userId, String name)
    {
        this.userId = userId;
        this.name = name;
    }

    public abstract String getRole();
}