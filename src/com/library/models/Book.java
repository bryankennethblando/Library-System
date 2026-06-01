package com.library.models;

public class Book
{
    protected String id;
    protected String title;
    protected String author;
    protected boolean isAvailable = true;

    public Book (String id, String title, String author)
    {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    // getters
    public String id() {return id;}
    public String title() {return title;}
    public String author() {return author;}
    public boolean isAvailable() {return isAvailable;}

    @Override
    public String toString() 
    {
        return id + "," + title + "," + author + "," + isAvailable;
    }
}