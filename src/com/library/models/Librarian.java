package com.library.models;

public class Librarian extends User
{
    private String employeeId;
    private int adminLevel;

    // user sub-class constructor
    Librarian (String userId, String name, String employeeId, int adminLevel)
    {
        // passing the required params
        super(userId, name);
        this.employeeId = employeeId;
        this.adminLevel = adminLevel;
    }

    // overriding the inherited abstract method
    @Override
    public String getRole()
    {
        return "Librarian";
    }

    // getters
    public String getEmployeeId() {return employeeId;}
    public int getAdminLevel() {return adminLevel;}
}
