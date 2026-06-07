package com.library.gui;

import java.awt.*;
import javax.swing.*;

import com.library.services.LibraryService;
import com.library.models.*;
import com.library.gui.LoginForm;

public class MainDashboard extends JFrame
{
    private JLabel welcomeLabel;
    private JTable bookTable;
    private JScrollPane scrollPane;
    private JTextField bookIdField;
    private JButton borrowButton;
    private JButton returnButton;

    private LibraryService libraryService;
    private String currentUserId;

    public MainDashboard(LibraryService libraryService, String loggedInUserId)
    {
        this.libraryService = libraryService;
        this.currentUserId = loggedInUserId;

        setTitle("Library Management Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        welcomeLabel = new JLabel("Welcome to Library Management Console");
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Book Id", "Title", "Author", "Status"};
        Object[][] data = {};

        bookTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 1, 10, 10));

        bookIdField = new JTextField("Enter Book Id: ");
        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");

        controlPanel.add(bookIdField);
        controlPanel.add(borrowButton);
        controlPanel.add(returnButton);
        add(controlPanel, BorderLayout.EAST);

        borrowButton.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            String userId = currentUserId;

            if (bookId.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter a Book ID.");
                return;
            }

            libraryService.borrowBook(userId, bookId);

            updateTableDisplay();

            bookIdField.setText("");
        });

        returnButton.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();

            if (bookId.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter a Book ID.");
                return;
            }

            libraryService.returnBook(bookId);

            updateTableDisplay();

            bookIdField.setText("");
        });
    }

    public void updateTableDisplay()
    {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);

        for (com.library.models.Book b : libraryService.getBookList())
        {
            Object[] rowData = 
            {
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.isAvailable() ? "Available" : "Borrowed"
            };
            model.addRow(rowData);
        }
    }
}
