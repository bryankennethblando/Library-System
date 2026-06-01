package com.library.models;

public class Student extends User
{
    protected int borrowedCount;

    Student (String userId, String name, int borrowedCount)
    {
        super(userId, name);
        this.borrowedCount = borrowedCount;
    }
    @Override
    public String getRole()
    {
        return "Student";
    }
}