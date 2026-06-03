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
    private final String usersPath = "data/user.csv";
    private final String booksPath = "data/book.csv";

    // constructor
    public LibraryService(User user, Book book)
    {
        // adding the object we get to the arraylist
        this.user.add(user);
        this.book.add(book);
    }

    // to add book or user object into their respective arraylist
    public void addUser(User u) {user.add(u);}
    public void addBook(Book b) {book.add(b);}

    // data writting
    public void savedBooks()
    {
        String booksData = "";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(booksPath)))
        {   
            // to iterate to 
            for (Book b : book)
            {
                booksData = b.getId() + "," +b.getTitle() + "," + b.getAuthor() + "," +b.isAvailable();

                // to write data in the csv file
                writer.write(booksData);
                writer.newLine();
            }
            System.out.println("💾 Data saved successfully to data/book.csv");
        } 
        catch (IOException e) 
        {
            System.out.println("❌ Error saving data: " + e.getMessage());
            e.getStackTrace();
        }
    }

    public void savedUsers()
    {
        String userData = "";   


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersPath)))
        {
            for (User u : user) 
            {
                if (u instanceof Student)
                {
                    Student s = (Student) u;
                    userData = s.getRole() + "," + s.getUserId() + "," + s.getName() + "," +
                            s.getBorrowedCount();
                }
                else if (u instanceof Librarian)
                {
                    Librarian l = (Librarian) u;
                    userData = l.getRole() + "," + l.getUserId() + "," + l.getName() + "," +
                            l.getEmployeeId() + "," + l.getAdminLevel();
                }
                writer.write(userData);
                writer.newLine();
            }
            System.out.println("💾 Data saved successfully to data/user.csv");
        } 
        catch (IOException e) 
        {
            System.out.println("❌ Error saving data: " + e.getMessage());
            e.getStackTrace();
        }
    }

    // data loading: reading the data on the csv files -> book.csv and user.csv
    public void loadBooks()
    {
        String line;
        String[] data = new String[3];

        File file = new File(booksPath);
        if (!file.exists()) {return;}

        try (BufferedReader read = new BufferedReader(new FileReader(booksPath)))
        {
            while((line = read.readLine()) != null)
            {
                // splitting the values of line that came from the csv using split()
                data = line.split(",");

                // getting the data using indices then appending it to the objects constructor 
                Book bookData = new Book(data[0], data[1], data[2]);
                bookData.setAvailability(Boolean.parseBoolean(data[3]));

                // adding the loaded data onto the book arraylist
                addBook(bookData);
            }
            System.out.println("💾 Data loaded successfully.");

        }
        catch (Exception e) 
        {
            System.out.println("❌ Failed to load the data from the previous process...." + e.getMessage());   
        }
    }

    public void loadUsers()
    {
        String line;
        String[] data = new String[4];

        File file = new File(usersPath);
        if (!file.exists()) {return;}

        try (BufferedReader read = new BufferedReader(new FileReader(usersPath)))
        {
            while ((line = read.readLine()) != null)
            {
                data = line.split(",");

                if (data[0].equalsIgnoreCase("Student"))
                {
                    Student studentData = new Student(data[1], data[2]);
                    studentData.setBorrowedCount(Integer.parseInt(data[3]));

                    addUser(studentData);
                }
                else if (data[0].equalsIgnoreCase("Librarian"))
                {
                    Librarian librarianData = new Librarian(data[1], data[2], data[3], Integer.parseInt(data[4]));

                    addUser(librarianData);

                }
            }
            System.out.println("💾 Data loaded successfully.");
        } 
        catch (Exception e) 
        {
            System.out.println("❌ Failed to load the data from the previous process...." + e.getMessage()); 
        }
    }

}