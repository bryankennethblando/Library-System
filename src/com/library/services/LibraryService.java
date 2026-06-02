package com.library.services;

import java.util.*;
import java.io.*;
import com.library.models.*;

public class LibraryService
{
    // an arraylist to holds the objects of book and user
    private ArrayList<User> user = new ArrayList<>(); // -> for users
    private ArrayList<Book> book = new ArrayList<>(); // -> for books

    // file path for saving the execution of file io
    private final String usersPath = "data/users.csv";
    private final String booksPath = "data/books.csv";

    // constructor
    public LibraryService(User user, Book book)
    {
        // adding the object we get to the arraylist
        this.user.add(user);
        this.book.add(book);
    }



}