package com.library.gui;

import java.awt.*;
import javax.swing.*;

import com.library.services.LibraryService;

public class LoginForm extends JFrame
{
    private JLabel titleLabel;
    private JLabel userPromptLabel;
    private JTextField userIdField;
    private JButton loginButton, exiButton, registerbButton;
    private JComboBox<String> roleSelector;

    private LibraryService libraryService;

    public LoginForm(LibraryService libraryService)
    {
        this.libraryService = libraryService;

        setTitle("Library Management System - Login");
        setSize(400, 25);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        titleLabel = new JLabel("Library Account Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2; 
        add(titleLabel, gbc);

        userPromptLabel = new JLabel("Enter UserId: ");
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        add(userPromptLabel, gbc);

        userIdField = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 1; 
        add(userIdField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        add(loginButton, gbc);

        exiButton = new JButton("Exit");
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        add(exiButton, gbc);

        JSeparator separator = new JSeparator();
        separator.setBounds(20, 160, 380, 10);
        add(separator);



        loginButton.addActionListener(e -> {
            String userId = userIdField.getText().trim();

            if (userId.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please Enter a User ID.");
                return;
            }

            boolean userExists = false;
            for (com.library.models.User u : libraryService.getUserList()) 
            {
                if (u.getUserId().equals(userId)) 
                {
                    userExists = true;
                    break;
                }
            }

            if (!userExists) 
            {
                JOptionPane.showMessageDialog(this, "Access Denied: Invalid User ID.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }

            MainDashboard dashboard = new MainDashboard(libraryService, userId);
            dashboard.updateTableDisplay();
            dashboard.setVisible(true);
            this.dispose();
        });

        

        exiButton.addActionListener(e -> System.exit(0));
    }
}