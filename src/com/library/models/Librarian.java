package com.library.models;

public class Librarian extends User
{
    protected String employeeId;
    protected int adminLevel;

    Librarian (String userId, String name, String employeeId, int adminLevel)
    {
        super(userId, name);
        this.employeeId = employeeId;
        this.adminLevel = adminLevel;
    }

    @Override
    public String getRole()
    {
        return "Librarian";
    }
}
