package com.library.models;

public class Book
{
    private String id;
    private String title;
    private String author;
    private boolean isAvailable;
    private String borrowerId;

    // parent class constructor
    public Book (String id, String title, String author)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.borrowerId = "None";
    }

    // setter
    public void setAvailability(boolean isAvailable) {this.isAvailable = isAvailable;}
    public void setBorrowedId(String borrowedId) {this.borrowerId = borrowedId;}

    // getters
    public String getId() {return id;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public boolean isAvailable() {return isAvailable;}
    public String getBorrowerId() {return borrowerId;}

    // overriding the toString for printing formatting of book details
    @Override
    public String toString() 
    {
        return id + "," + title + "," + author + "," + isAvailable + "," + borrowerId;
    }
}