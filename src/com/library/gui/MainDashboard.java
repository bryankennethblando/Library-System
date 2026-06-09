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
    private JButton studentExitButton;
    private JPanel controlPanel;
    private JPanel studentPanel;

    // for librarian dashboard
    private JPanel adminPanel;
    private JPanel adminRemovePanel;
    private JTextField newBookIdField;
    private JTextField newTitleField;
    private JTextField newAuthorField;
    private JTextField removeBookField;
    private JButton addBookButton;
    private JButton removeBookButton;
    private JButton adminExitButton;

    private LibraryService libraryService;
    private LoginForm login;
    private String currentUserId;

    public MainDashboard(LibraryService libraryService, LoginForm login, String loggedInUserId)
    {
        this.libraryService = libraryService;
        this.login = login;
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

        JPanel studentExitPanel = new JPanel(new GridLayout(4, 1, 0, 1));
        studentExitPanel.setBorder(BorderFactory.createTitledBorder("Back to Login Menu"));

        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        studentExitButton = new JButton("Exit");

        studentPanel.add(fieldWrapper);
        studentPanel.add(borrowButton);
        studentPanel.add(returnButton);

        studentExitPanel.add(studentExitButton);

        controlPanel.add(studentPanel);
        controlPanel.add(studentExitPanel);
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

        adminRemovePanel = new JPanel(new GridLayout(4, 1, 0, 10));
        adminRemovePanel.setBorder(BorderFactory.createTitledBorder("Remove Book"));

        newBookIdField = new JTextField(15);
        newTitleField = new JTextField(15);
        newAuthorField = new JTextField(15);
        removeBookField = new JTextField(15);
        addBookButton = new JButton("Add New Book");
        removeBookButton = new JButton("Remove Book");
        adminExitButton = new JButton("Exit");

        JPanel idWrap = new JPanel(new BorderLayout());
        idWrap.add(new JLabel("New Book ID:"), BorderLayout.NORTH);
        idWrap.add(newBookIdField, BorderLayout.CENTER);

        JPanel titleWrap = new JPanel(new BorderLayout());
        titleWrap.add(new JLabel("Book Title:"), BorderLayout.NORTH);
        titleWrap.add(newTitleField, BorderLayout.CENTER);

        JPanel authorWrap = new JPanel(new BorderLayout());
        authorWrap.add(new JLabel("Author Name:"), BorderLayout.NORTH);
        authorWrap.add(newAuthorField, BorderLayout.CENTER);

        JPanel removeWrap= new JPanel(new BorderLayout());
        removeWrap.add(new JLabel("Target Book ID:"), BorderLayout.NORTH);
        removeWrap.add(removeBookField, BorderLayout.CENTER);

        adminPanel.add(idWrap);
        adminPanel.add(titleWrap);
        adminPanel.add(authorWrap);
        adminPanel.add(addBookButton);

        adminRemovePanel.add(removeWrap);
        adminRemovePanel.add(removeBookButton);
        adminRemovePanel.add(adminExitButton);

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.add(studentPanel);
        centerStack.add(Box.createVerticalStrut(15));
        centerStack.add(adminPanel);
        centerStack.add(adminRemovePanel);

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

            newBookIdField.setText("");
            newTitleField.setText("");
            newAuthorField.setText("");

            JOptionPane.showMessageDialog(
                this,
                "New added book is saved successfully.",
                "System Notification",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        removeBookButton.addActionListener(e -> {
            String inputBookId = removeBookField.getText().trim();

            if (inputBookId.isEmpty())
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in the Target Book Id to remove the book.",
                    "System Notice",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            libraryService.removeBook(inputBookId);
            updateTableDisplay();

            removeBookField.setText("");

            JOptionPane.showMessageDialog(
                this,
                "Removing Book Id - " + inputBookId + " successfully.",
                "System Notification",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // for student exit button back to login form
        studentExitButton.addActionListener(e ->{
            this.login.setVisible(true);
            this.dispose();
        });

        // for librarian/admin exit button back to login form
        adminExitButton.addActionListener(e ->{
            this.login.setVisible(true);
            this.dispose();
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
            adminRemovePanel.setVisible(false);
            adminExitButton.setVisible(false);

            // setting it true for student field
            studentPanel.setVisible(true);
            borrowButton.setVisible(true);
            returnButton.setVisible(true);
            studentExitButton.setVisible(true);

            Student s = (Student) user;
            welcomeLabel.setText("Welcome, " + s.getName() + "- Student || " +
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
            adminExitButton.setVisible(true);

            // setting it false for student fields
            studentPanel.setVisible(false);
            borrowButton.setVisible(false);
            returnButton.setVisible(false);
            studentExitButton.setVisible(false);
            
            Librarian l = (Librarian) user;
            welcomeLabel.setText("Welcome, " + l.getName() + "- Librarian Desk ");
        }
    }
}
