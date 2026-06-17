package com.library;

import com.library.services.LibraryService;
import com.library.gui.LoginForm;


public class Main
{
    public static void main(String args[])
    {
        // instantiating the service class
        LibraryService service = new LibraryService();

        // using the file i/o we load the existing data to library service
        // to have a visual data representation on the the gui below
        service.loadBooks();
        service.loadUsers();

        // invoking the gui class: starting from the login form
        // the gui is connected through the "this" argument on their
        // respective constructor
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm(service);
            loginForm.setVisible(true);
        });
    }
}