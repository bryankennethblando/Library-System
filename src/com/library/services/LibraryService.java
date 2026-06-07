package com.library.services;

import java.util.*;
import java.io.*;
import javax.swing.*;

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
    public LibraryService()
    {
        // Keeps collections initialized empty, for file load inputs
    }

    // to add book or user object into their respective arraylist
    public void addUser(User u) {user.add(u);}
    public void addBook(Book b) {book.add(b);}

    // getters
    public ArrayList<Book> getBookList() {return book;}
    public ArrayList<User> getUserList() {return user;}
 
    // data writting
    public void savedBooks()
    {
        String booksData = "";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(booksPath)))
        {   
            // to iterate to 
            for (Book b : book)
            {
                booksData = b.toString();

                // to write data in the csv file
                writer.write(booksData);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(
                null,
                "💾 Data saved successfully to data/book.csv",
                "System Update",
                JOptionPane.INFORMATION_MESSAGE
            );
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(
                null,
                "❌ Error saving data: " + e.getMessage(),
                "Library Security Alert",
                JOptionPane.ERROR_MESSAGE
            );
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
                    userData = s.toCSV();
                }
                else if (u instanceof Librarian)
                {
                    Librarian l = (Librarian) u;
                    userData = l.toCSV();
                }
                writer.write(userData);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(
                null,
                "💾 Data saved successfully to data/user.csv",
                "System Update",
                JOptionPane.INFORMATION_MESSAGE
            );
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(
                null,
                "❌ Error saving data: " + e.getMessage(),
                "Library Security Alert",
                JOptionPane.ERROR_MESSAGE
            );
            e.getStackTrace();
        }
    }

    // data loading: reading the data on the csv files -> book.csv and user.csv
    public void loadBooks()
    {
        String line;
        String[] data = new String[4];

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
                bookData.setBorrowedId(data[4]);

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

    public void borrowBook(String userId, String bookId)
    {
        User targetUser = null;
        Book targetBook = null;

        for (User u : user)
        {
            if (u.getUserId().equals(userId))
            {
                targetUser = u;
                break;
            }
        }

        for (Book b : book)
        {
            if (b.getId().equals(bookId))
            {
                targetBook = b;
                break;
            }
        }

        // validation
        if (targetUser == null)
        {
            System.out.println("Error: No user found with that userId.");
        }

        if (targetBook == null)
        {
            System.out.println("Erro: No book found with that bookId.");
        }

        if (targetUser instanceof Student)
        {
            Student s = (Student) targetUser;

            if (s.getBorrowedCount() >= 3)
            {
                System.out.println("You've exceed the book borrowed limit.");
                System.out.println("Return one borrowed book to be able to borrow again.");
                return;
            }

            if (!targetBook.isAvailable())
            {
                System.out.println("The book you want to borrow is check out");
            }

            targetBook.setAvailability(false);
            targetBook.setBorrowedId(userId);
            s.setBorrowedCount(s.getBorrowedCount() + 1);

            System.out.println("Success: " + targetBook.getTitle() + " is borrowed by " +
                                s.getName());

            savedBooks();
            savedUsers();
        }
        else if (targetUser instanceof Librarian)
        {
            System.out.println("Librarians cannot borrow books under a student profile configuration.");
        }
    }

    public void returnBook(String bookId)
    {
        Book targetBook = null;
        String borrowerId = "";

        for (Book b : book)
        {
            if (b.getId().equals(bookId))
            {
                targetBook = b;
                borrowerId = b.getBorrowerId();
                break;
            }
        }

        for (User u : user)
        {
            if (u.getUserId().equals(borrowerId))
            {
                if (u instanceof Student)
                {
                    Student s = (Student) u;

                    if (s.getBorrowedCount() > 0)
                    {
                        s.setBorrowedCount(s.getBorrowedCount() - 1);
                    }
                }
                break;
            }
        }

        targetBook.setAvailability(true);
        targetBook.setBorrowedId("None");

        System.out.println("Success: Book record updated cleanly back into stock files.");

        savedBooks();
        savedUsers();
    }
}