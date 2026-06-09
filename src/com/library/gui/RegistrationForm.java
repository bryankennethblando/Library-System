package com.library.gui;

import javax.swing.*;
import java.awt.*;

import com.library.services.LibraryService;
import com.library.models.*;


public class RegistrationForm extends JFrame
{
    private JButton exitButton;
    private JButton registerButton;
    private JComboBox<String> roleSelector;

    private LibraryService libraryService;
    private LoginForm login;

    public RegistrationForm(LibraryService libraryService, LoginForm login)
    {
        this.libraryService = libraryService;
        this.login = login;

        setTitle("Library Management System - Register");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel registrationPanel = new JPanel(new GridBagLayout());
        registrationPanel.setBounds(20, 180, 380, 210);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(5, 5, 5, 5); 
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        JLabel regTitleLabel = new JLabel("Create New Library Account", SwingConstants.CENTER);
        regTitleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        gbc1.gridx = 0; 
        gbc1.gridy = 0; 
        gbc1.gridwidth = 2;
        registrationPanel.add(regTitleLabel, gbc1);

        JLabel id = new JLabel("Enter ID:");
        gbc1.gridx = 0; 
        gbc1.gridy = 1; 
        gbc1.gridwidth = 1;
        registrationPanel.add(id, gbc1);

        JTextField regId = new JTextField();
        gbc1.gridx = 1; 
        gbc1.gridy = 1;
        registrationPanel.add(regId, gbc1);

        JLabel name = new JLabel("Enter Full Name:");
        gbc1.gridx = 0; 
        gbc1.gridy = 2;
        registrationPanel.add(name, gbc1);

        JTextField regName = new JTextField();
        gbc1.gridx = 1; 
        gbc1.gridy = 2;
        registrationPanel.add(regName, gbc1);

        JLabel role = new JLabel("Account Type:");
        gbc1.gridx = 0; 
        gbc1.gridy = 3;
        registrationPanel.add(role, gbc1);

        // Dropdown options matching the system user:
        String[] roles = {"Student", "Librarian"};
        roleSelector = new JComboBox<>(roles);
        gbc1.gridx = 1; 
        gbc1.gridy = 3;
        registrationPanel.add(roleSelector, gbc1);

        registerButton = new JButton("Register");
        gbc1.gridx = 0;
        gbc1.gridy = 4; 
        gbc1.gridwidth = 2;
        registrationPanel.add(registerButton, gbc1);

        exitButton = new JButton("Exit");
        gbc1.gridx = 0;
        gbc1.gridy = 5; 
        gbc1.gridwidth = 3;
        registrationPanel.add(exitButton, gbc1);

        JPanel mainContainer = new JPanel(new GridBagLayout());
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.insets = new Insets(10, 10, 10, 10); 

        mainGbc.gridy = 0;
        mainContainer.add(registrationPanel, mainGbc);

        add(mainContainer);

        registerButton.addActionListener(e ->{
            String inputId = regId.getText().trim();
            String inputName = regName.getText().trim();
            String selectedRole = (String) roleSelector.getSelectedItem();

            if (inputId.isEmpty() || inputName.isEmpty())
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all the registration field",
                    "Validation Warning",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            for (User u : libraryService.getUserList())
            {
                if (u.getUserId().equals(inputId))
                {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error: User Id " + inputId + " is already taken.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            User newUser;
            if (selectedRole.equals("Student"))
            {
                newUser = new Student(inputId, inputName);
            }
            else
            {
                newUser = new Librarian(inputId, inputName, "Staff-" + inputId, 1);
            }

            libraryService.addUser(newUser);
            libraryService.savedUsers();

            JOptionPane.showMessageDialog(
                this,
                "Account successfully created. You can now log-in using this userId.",
                "Registration Complete",
                JOptionPane.INFORMATION_MESSAGE
            );

            regId.setText("");
            regName.setText("");
        });

        exitButton.addActionListener(e -> {
            this.login.setVisible(true);
            this.dispose();
        });
    }
}