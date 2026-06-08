package com.library.gui;

import java.awt.*;
import javax.swing.*;

import com.library.services.LibraryService;

public class LoginForm extends JFrame
{
    private JLabel titleLabel;
    private JLabel userPromptLabel;
    private JTextField userIdField;
    private JButton loginButton;

    private LibraryService libraryService;

    public LoginForm(LibraryService libraryService)
    {
        this.libraryService = libraryService;

        setTitle("Library Management System - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        titleLabel = new JLabel("Library Account Login", SwingConstants.CENTER);
        titleLabel.setBounds(50, 20, 300, 30);
        add(titleLabel);

        userPromptLabel = new JLabel("Enter UserId: ");
        userPromptLabel.setBounds(40, 70, 100, 25);
        add(userPromptLabel);

        userIdField = new JTextField();
        userIdField.setBounds(150, 70, 180, 25);
        add(userIdField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 110, 100, 30);
        add(loginButton);

        loginButton.addActionListener(e -> {
            String userId = userIdField.getText().trim();

            if (userId.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter a User ID.");
                return;
            }

            MainDashboard dashboard = new MainDashboard(libraryService, userId);
            dashboard.setVisible(true);
            this.dispose();
        });
    }
}