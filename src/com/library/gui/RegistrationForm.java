package com.library.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import com.library.services.LibraryService;
import com.library.models.*;

public class RegistrationForm extends JFrame 
{
    private JButton exitButton;
    private JButton registerButton;
    private JComboBox<String> roleSelector;
    private JLabel regTitleLabel, idLabel, nameLabel, roleLabel;
    private JTextField regId, regName;

    private LibraryService libraryService;
    private LoginForm login;

    public RegistrationForm(LibraryService libraryService, LoginForm login) 
    {
        this.libraryService = libraryService;
        this.login = login;

        // --- 1. Window Configuration ---
        setTitle("Library Management System - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true); 
        setSize(600, 600); // Balanced starting size

        // --- 2. Structural Containers ---
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(new Color(225, 230, 235)); // Universal accent gray background

        JPanel regCard = new JPanel(new GridBagLayout());
        regCard.setBackground(Color.WHITE); // White form card block layout
        regCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 205, 210), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        // Header Title
        regTitleLabel = new JLabel("Create New Library Account", SwingConstants.CENTER);
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(10, 12, 25, 12);
        regCard.add(regTitleLabel, gbc);

        // Reset spacing constraints for data inputs
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.gridwidth = 1;

        // ID Form row
        idLabel = new JLabel("Enter ID:", SwingConstants.RIGHT);
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        regCard.add(idLabel, gbc);

        regId = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 1;
        regCard.add(regId, gbc);

        // Name Form row
        nameLabel = new JLabel("Enter Full Name:", SwingConstants.RIGHT);
        gbc.gridx = 0; 
        gbc.gridy = 2;
        regCard.add(nameLabel, gbc);

        regName = new JTextField();
        gbc.gridx = 1; 
        gbc.gridy = 2;
        regCard.add(regName, gbc);

        // Role Selector Dropdown Row
        roleLabel = new JLabel("Account Type:", SwingConstants.RIGHT);
        gbc.gridx = 0; 
        gbc.gridy = 3;
        regCard.add(roleLabel, gbc);

        String[] roles = {"Student", "Librarian"};
        roleSelector = new JComboBox<>(roles);
        gbc.gridx = 1; 
        gbc.gridy = 3;
        regCard.add(roleSelector, gbc);

        // Action Menu Options
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        registerButton = new JButton("Register");
        gbc.gridy = 4; 
        regCard.add(registerButton, gbc);

        exitButton = new JButton("Back to Login Menu");
        gbc.gridy = 5; 
        regCard.add(exitButton, gbc);

        // Render card centered inside screen bounds
        mainContainer.add(regCard, new GridBagConstraints());
        add(mainContainer);
        setLocationRelativeTo(null);

        // --- 3. RESPONSIVE TYPOGRAPHY SCALING ENGINE ---
        this.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) {
                int benchmark = Math.min(getWidth(), getHeight());
                int dynamicFont = Math.max(14, benchmark / 32); 

                // Scaling fonts
                regTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, (int)(dynamicFont * 1.5)));
                idLabel.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                nameLabel.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                roleLabel.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                regId.setFont(new Font("Segoe UI", Font.PLAIN, dynamicFont));
                regName.setFont(new Font("Segoe UI", Font.PLAIN, dynamicFont));
                roleSelector.setFont(new Font("Segoe UI", Font.PLAIN, dynamicFont));
                registerButton.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));
                exitButton.setFont(new Font("Segoe UI", Font.BOLD, dynamicFont));

                // Control component heights cleanly
                int inputHeight = (int)(dynamicFont * 2.2);
                int buttonHeight = (int)(dynamicFont * 2.6);
                int cardWidth = (int)(benchmark * 0.78);

                regId.setPreferredSize(new Dimension(150, inputHeight));
                regName.setPreferredSize(new Dimension(150, inputHeight));
                roleSelector.setPreferredSize(new Dimension(150, inputHeight));
                registerButton.setPreferredSize(new Dimension(cardWidth, buttonHeight));
                exitButton.setPreferredSize(new Dimension(cardWidth, buttonHeight));
                
                regCard.setPreferredSize(new Dimension(cardWidth, (int)(benchmark * 0.75)));
                regCard.revalidate();
            }
        });

        // --- 4. System Logic Drivers ---
        registerButton.addActionListener(e -> {
            String inputId = regId.getText().trim();
            String inputName = regName.getText().trim();
            String selectedRole = (String) roleSelector.getSelectedItem();

            if (inputId.isEmpty() || inputName.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please fill in all the registration fields.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (User u : libraryService.getUserList()) 
            {
                if (u.getUserId().equals(inputId)) {
                    JOptionPane.showMessageDialog(this, "Error: User ID " + inputId + " is already taken.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            User newUser = selectedRole.equals("Student") ? new Student(inputId, inputName) : new Librarian(inputId, inputName, "Staff-" + inputId, 1);
            libraryService.addUser(newUser);
            libraryService.savedUsers();

            JOptionPane.showMessageDialog(this, "Account successfully created. You can now log-in.", "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            regId.setText("");
            regName.setText("");
        });

        exitButton.addActionListener(e -> {
            this.login.setVisible(true);
            this.dispose();
        });
    }
}