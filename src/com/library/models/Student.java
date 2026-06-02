package com.library.models;

public class Student extends User
{
    private int borrowedCount;

    // setter
    public void setBorrowedCount(int borrowedCount) {this.borrowedCount = borrowedCount;}

    // constructor
    Student (String userId, String name)
    {
        // the required params for the parent class
        super(userId, name);
        this.borrowedCount = 0;
    }

    // overriding the abstract method from the parent class
    @Override
    public String getRole()
    {
        return "Student";
    }

    // getters
    public int getBorrowedCount() {return borrowedCount;}
}