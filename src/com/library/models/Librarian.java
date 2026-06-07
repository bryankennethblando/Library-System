package com.library.models;

public class Librarian extends User
{
    private String employeeId;
    private int adminLevel;

    // user sub-class constructor
    public Librarian (String userId, String name, String employeeId, int adminLevel)
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

    @Override
    public String toCSV()
    {
        return getRole() + "," +getUserId() + "," + getName() + "," + employeeId + "," + adminLevel; 
    }

    // getters
    public String getEmployeeId() {return employeeId;}
    public int getAdminLevel() {return adminLevel;}
}
