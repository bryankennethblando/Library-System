package com.library.gui;

import java.awt.*;
import javax.swing.*;

import com.library.services.LibraryService;
import com.library.models.*;

public class MainDashboard extends JFrame
{
    // for student dashboard
    private JLabel welcomeLabel;
    private JTable bookTable;
    private JScrollPane scrollPane;
    private JTextField bookIdField;
    private JButton borrowButton;
    private JButton returnButton;
    private JPanel controlPanel;
    private JPanel studentPanel;

    // for librarian dashboard
    private JPanel adminPanel;
    private JTextField newBookIdField;
    private JTextField newTitleField;
    private JTextField newAuthorField;
    private JButton addBookButton;

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

        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(data, columnNames);
        bookTable = new JTable(tableModel);
        scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Action"));

        bookIdField = new JTextField(15);
        bookIdField.setText("");

        JPanel fieldWrapper = new JPanel(new BorderLayout());
        fieldWrapper.add(new JLabel("Target Book ID:"), BorderLayout.NORTH);
        fieldWrapper.add(bookIdField, BorderLayout.CENTER);

        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");

        studentPanel.add(fieldWrapper);
        studentPanel.add(borrowButton);
        studentPanel.add(returnButton);
        controlPanel.add(studentPanel);
        add(controlPanel, BorderLayout.EAST);

        borrowButton.addActionListener(e -> {
            String inputBookId = bookIdField.getText().trim();
            String inputUserId = currentUserId;

            if (inputBookId.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter a Book ID.");
                return;
            }

            libraryService.borrowBook(inputUserId, inputBookId);

            updateTableDisplay();

            bookIdField.setText("");
        });

        returnButton.addActionListener(e -> {
            String inputBookId = bookIdField.getText().trim();

            if (inputBookId.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter a Book ID.");
                return;
            }

            libraryService.returnBook(inputBookId);

            updateTableDisplay();

            bookIdField.setText("");
        });


        // librarian interface
        adminPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        adminPanel.setBorder(BorderFactory.createTitledBorder("Librarian Desk Admin"));

        newBookIdField = new JTextField(15);
        newTitleField = new JTextField(15);
        newAuthorField = new JTextField(15);
        addBookButton = new JButton("Add New Book");

        JPanel idWrap = new JPanel(new BorderLayout());
        idWrap.add(new JLabel("New Book ID:"), BorderLayout.NORTH);
        idWrap.add(newBookIdField, BorderLayout.CENTER);

        JPanel titleWrap = new JPanel(new BorderLayout());
        titleWrap.add(new JLabel("Book Title:"), BorderLayout.NORTH);
        titleWrap.add(newTitleField, BorderLayout.CENTER);

        JPanel authorWrap = new JPanel(new BorderLayout());
        authorWrap.add(new JLabel("Author Name:"), BorderLayout.NORTH);
        authorWrap.add(newAuthorField, BorderLayout.CENTER);

        adminPanel.add(idWrap);
        adminPanel.add(titleWrap);
        adminPanel.add(authorWrap);
        adminPanel.add(addBookButton);

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.add(studentPanel);
        centerStack.add(Box.createVerticalStrut(15));
        centerStack.add(adminPanel);

        controlPanel.add(centerStack, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.EAST);

        addBookButton.addActionListener(e -> {
            String inputBookId = newBookIdField.getText().trim();
            String inputTitle = newTitleField.getText().trim();
            String inputAuthor = newAuthorField.getText().trim();

            if (inputBookId.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter a Book ID for newly Book.");
                return;
            }

            if (inputTitle.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter the Title of the Book.");
                return;
            }

            if (inputAuthor.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please Enter the Author of this Book.");
                return;
            }

            com.library.models.Book b = new Book(inputBookId, inputTitle, inputAuthor);

            libraryService.addBook(b);
            libraryService.savedBooks();

            updateTableDisplay();

            JOptionPane.showMessageDialog(
                this,
                "New added book is saved sucessfully.",
                "System Notification",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        User logInUser = null;
        for (User u : libraryService.getUserList())
        {
            if(u.getUserId().equals(currentUserId))
            {
                logInUser = u;
                break;
            }
        }

        configureRoleBasedAccess(logInUser);
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

    public void configureRoleBasedAccess(User user)
    {
        if (user == null) {return;}

        if (user instanceof Student)
        {
            // setting it false for librarian fields
            adminPanel.setVisible(false);
            newBookIdField.setVisible(false);
            newTitleField.setVisible(false);
            newAuthorField.setVisible(false);
            addBookButton.setVisible(false);

            // setting it true for student field
            borrowButton.setVisible(true);
            returnButton.setVisible(true);

            Student s = (Student) user;
            welcomeLabel.setText("Welcome, " + s.getName() + "- Student " +
                            "Borrowed: " + s.getBorrowedCount() + "/3");
        }
        else if (user instanceof Librarian)
        {
            // setting true for all admin fields
            adminPanel.setVisible(true);
            newBookIdField.setVisible(true);
            newTitleField.setVisible(true);
            newAuthorField.setVisible(true);
            addBookButton.setVisible(true);

            // setting it false for student fields
            studentPanel.setVisible(false);
            borrowButton.setVisible(false);
            returnButton.setVisible(false);
            
            Librarian l = (Librarian) user;
            welcomeLabel.setText("Welcome, " + l.getName() + "- Librarian Desk ");
        }
    }
}
