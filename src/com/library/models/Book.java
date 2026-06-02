package com.library.models;

public class Book
{
    private String id;
    private String title;
    private String author;
    private boolean isAvailable;

    // parent class constructor
    public Book (String id, String title, String author)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    // setter
    public void setAvailability(boolean isAvailable) {this.isAvailable = isAvailable;}

    // getters
    public String getId() {return id;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public boolean isAvailable() {return isAvailable;}

    // overriding the toString for printing formatting of book details
    @Override
    public String toString() 
    {
        return id + "," + title + "," + author + "," + isAvailable;
    }
}