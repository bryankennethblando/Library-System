package com.library.gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

        // --- 1. Window Constraints ---
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); 
        setSize(600, 550); // Balanced starting size

        // --- 2. Layout Configurations ---
        // mainContainer holds the loginCard centered on full screen
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(new Color(225, 230, 235)); // Slightly darker background

        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(Color.WHITE); // White card overlay effect
        loginCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 205, 210), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch wide, but NOT tall
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Prevent vertical auto-distortion

        // Title Header
        titleLabel = new JLabel("Library Account Login", SwingConstants.CENTER);
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2; 
        loginCard.add(titleLabel, gbc);

        // Input Row
        gbc.gridwidth = 1;

        userPromptLabel = new JLabel("Enter User ID: ", SwingConstants.RIGHT);
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        loginCard.add(userPromptLabel, gbc);

        userIdField = new JTextField(15);
        gbc.gridx = 1; 
        gbc.gridy = 1; 
        loginCard.add(userIdField, gbc);

        // Action Buttons
        gbc.gridx = 0; 
        gbc.gridwidth = 2; 

        loginButton = new JButton("Login");
        gbc.gridy = 2; 
        loginCard.add(loginButton, gbc);

        signupButton = new JButton("Sign-up");
        gbc.gridy = 3; 
        loginCard.add(signupButton, gbc);

        exitButton = new JButton("Exit Application");
        gbc.gridy = 4; 
        loginCard.add(exitButton, gbc);
      
        // Center the login card inside the main window container
        mainContainer.add(loginCard, new GridBagConstraints());
        add(mainContainer);
        setLocationRelativeTo(null);
        
        // --- 3. DYNAMIC FONT SCALING ENGINE ---
        // This watches the window size and scales typography instantly on resize/maximize
       this.addComponentListener(new ComponentAdapter() 
       {
            @Override
            public void componentResized(ComponentEvent e) {
                int benchmark = Math.min(getWidth(), getHeight());
                int dynamicFont = Math.max(14, benchmark / 32); 

                // 1. Scale Typography Cleanly
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, (int)(dynamicFont * 1.6)));
                userPromptLabel.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                userIdField.setFont(new Font("Segoe UI", Font.PLAIN, dynamicFont));
                loginButton.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                signupButton.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                exitButton.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));

                // 2. Control Element Heights Proportionally (Prevents giant distorted boxes)
                int inputHeight = (int)(dynamicFont * 2.2);
                int buttonHeight = (int)(dynamicFont * 2.6);
                int cardWidth = (int)(benchmark * 0.75);

                userIdField.setPreferredSize(new Dimension(150, inputHeight));
                loginButton.setPreferredSize(new Dimension(cardWidth, buttonHeight));
                signupButton.setPreferredSize(new Dimension(cardWidth, buttonHeight));
                exitButton.setPreferredSize(new Dimension(cardWidth, buttonHeight));
                
                // Force layout update with new bounds
                loginCard.setPreferredSize(new Dimension(cardWidth, (int)(benchmark * 0.7)));
                loginCard.revalidate();
            }
        });

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