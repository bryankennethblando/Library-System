package com.library.gui;

import java.awt.*;
import javax.swing.*;

import com.library.services.LibraryService;
import com.library.models.*;

public class LoginForm extends JFrame
{
    private JLabel titleLabel;
    private JLabel userPromptLabel;
    private JTextField userIdField;
    private JButton loginButton, exitButton, signupButton;

    private LibraryService libraryService;

    public LoginForm(LibraryService libraryService)
    {
        this.libraryService = libraryService;

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBounds(20, 180, 380, 210);

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
        loginPanel.add(titleLabel, gbc);

        userPromptLabel = new JLabel("Enter UserId: ");
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        loginPanel.add(userPromptLabel, gbc);

        userIdField = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 1; 
        loginPanel.add(userIdField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        loginPanel.add(loginButton, gbc);

        signupButton = new JButton("Sign-up");
        gbc.gridx = 0; 
        gbc.gridy = 3; 
        gbc.gridwidth = 3; 
        loginPanel.add(signupButton, gbc);

        exitButton = new JButton("Exit");
        gbc.gridx = 0; 
        gbc.gridy = 4; 
        gbc.gridwidth = 4; 
        loginPanel.add(exitButton, gbc);
      
        JPanel mainContainer = new JPanel(new GridBagLayout());
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.insets = new Insets(10, 10, 10, 10); 

        mainGbc.gridy = 0;
        mainContainer.add(loginPanel, mainGbc);

        add(mainContainer);

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

            MainDashboard dashboard = new MainDashboard(libraryService, this, userId);
            dashboard.updateTableDisplay();
            dashboard.setVisible(true);
            this.dispose();
        });

        signupButton.addActionListener(e ->{
            RegistrationForm registration = new RegistrationForm(libraryService, this);
            registration.setVisible(true);
            this.setVisible(false);
        });

        exitButton.addActionListener(e -> System.exit(0));

    }
}