package com.library;

import com.library.services.LibraryService;
import com.library.gui.LoginForm;


public class Main
{
    public static void main(String args[])
    {
        LibraryService service = new LibraryService();

        service.loadBooks();
        service.loadUsers();

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm(service);
            loginForm.setVisible(true);
        });
    }
}