package com.library.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import com.library.services.LibraryService;
import com.library.models.*;

public class MainDashboard extends JFrame 
{
    private JLabel welcomeLabel;
    private JTable bookTable;
    private JScrollPane scrollPane;
    private JTextField bookIdField, newBookIdField, newTitleField, newAuthorField, removeBookField;
    private JButton borrowButton, returnButton, studentExitButton, addBookButton, removeBookButton, adminExitButton;
    private JPanel controlPanel, studentPanel, adminPanel, adminRemovePanel, centerStack, topPanel;
    private JLabel targetBookLabel, newBookIdLabel, bookTitleLabel, authorNameLabel, targetRemoveLabel;

    private LibraryService libraryService;
    private LoginForm login;
    private String currentUserId;

    public MainDashboard(LibraryService libraryService, LoginForm login, String loggedInUserId) 
    {
        this.libraryService = libraryService;
        this.login = login;
        this.currentUserId = loggedInUserId;

        // --- 1. Main System Frame Bounds ---
        setTitle("Library Management Dashboard");
        setSize(1100, 700); // Expanded canvas for data visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setBackground(new Color(44, 62, 80)); // Dark sleek banner accent
        welcomeLabel = new JLabel("Welcome to Library Management Console");
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. Inventory Display Table Data ---
        String[] columnNames = {"Book ID", "Title", "Author", "Status"};
        Object[][] data = {};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(data, columnNames);
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(28); // Enhanced text cell row separation heights
        scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // --- 3. Interaction Control Side Panel Setup ---
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        controlPanel.setBackground(new Color(240, 243, 244));

        // STUDENT ACCOUNT PANELS
        studentPanel = new JPanel(new GridLayout(4, 1, 0, 12));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Actions Workspace"));

        bookIdField = new JTextField(15);
        JPanel fieldWrapper = new JPanel(new BorderLayout());
        targetBookLabel = new JLabel("Target Book ID:");
        fieldWrapper.add(targetBookLabel, BorderLayout.NORTH);
        fieldWrapper.add(bookIdField, BorderLayout.CENTER);

        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        studentExitButton = new JButton("Log Out of Session");
        studentExitButton.setBackground(new Color(192, 57, 43));
        studentExitButton.setForeground(Color.WHITE);

        studentPanel.add(fieldWrapper);
        studentPanel.add(borrowButton);
        studentPanel.add(returnButton);
        studentPanel.add(studentExitButton);

        // LIBRARIAN MANAGEMENT DESK PANELS
        adminPanel = new JPanel(new GridLayout(7, 1, 0, 8));
        adminPanel.setBorder(BorderFactory.createTitledBorder("Librarian Acquisition Entry"));

        newBookIdField = new JTextField(15);
        newTitleField = new JTextField(15);
        newAuthorField = new JTextField(15);
        addBookButton = new JButton("Add Book Record");
        addBookButton.setBackground(new Color(39, 174, 96));
        addBookButton.setForeground(Color.WHITE);

        newBookIdLabel = new JLabel("New Book ID:");
        bookTitleLabel = new JLabel("Book Title:");
        authorNameLabel = new JLabel("Author Name:");

        adminPanel.add(newBookIdLabel); 
        adminPanel.add(newBookIdField);
        adminPanel.add(bookTitleLabel); 
        adminPanel.add(newTitleField);
        adminPanel.add(authorNameLabel); 
        adminPanel.add(newAuthorField);
        adminPanel.add(addBookButton);

        adminRemovePanel = new JPanel(new GridLayout(4, 1, 0, 12));
        adminRemovePanel.setBorder(BorderFactory.createTitledBorder("De-catalog Disposal Desk"));
        
        removeBookField = new JTextField(15);
        removeBookButton = new JButton("Remove Book Record");
        removeBookButton.setBackground(new Color(211, 84, 0));
        removeBookButton.setForeground(Color.WHITE);
        adminExitButton = new JButton("Close System Console");
        adminExitButton.setBackground(new Color(192, 57, 43));
        adminExitButton.setForeground(Color.WHITE);
        
        targetRemoveLabel = new JLabel("Target Disposal Book ID:");
        adminRemovePanel.add(targetRemoveLabel);
        adminRemovePanel.add(removeBookField);
        adminRemovePanel.add(removeBookButton);
        adminRemovePanel.add(adminExitButton);

        // Stack containers cleanly using Box layouts
        centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.add(studentPanel);
        centerStack.add(adminPanel);
        centerStack.add(Box.createVerticalStrut(10));
        centerStack.add(adminRemovePanel);
        centerStack.add(Box.createVerticalStrut(10));
        

        controlPanel.add(centerStack, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.EAST);
        setLocationRelativeTo(null);

        // --- 4. FLUID DASHBOARD TYPOGRAPHY SCALING ENGINE ---
        this.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameWidth = getWidth();
                int dynamicFont = Math.max(13, frameWidth / 85);

                // Set dynamic tracking typography configurations
                Font headerFont = new Font("Segoe UI", Font.BOLD, (int)(dynamicFont * 1.4));
                Font uiBoldFont = new Font("Segoe UI", Font.BOLD, dynamicFont);
                Font uiPlainFont = new Font("Segoe UI", Font.PLAIN, dynamicFont);

                welcomeLabel.setFont(headerFont);
                bookTable.setFont(uiPlainFont);
                bookTable.getTableHeader().setFont(uiBoldFont);
                bookTable.setRowHeight((int)(dynamicFont * 1.8));
                int buttonHeight = (int)(dynamicFont * 2.6);


                // Form label configuration maps
                JLabel[] labels = {targetBookLabel, newBookIdLabel, bookTitleLabel, authorNameLabel, targetRemoveLabel};
                for (JLabel lbl : labels) lbl.setFont(uiBoldFont);

                // Form text input field maps
                JTextField[] fields = {bookIdField, newBookIdField, newTitleField, newAuthorField, removeBookField};
                for (JTextField f : fields) f.setFont(uiPlainFont);

                // System action trigger button maps
                JButton[] buttons = {borrowButton, returnButton, studentExitButton, addBookButton, removeBookButton, adminExitButton};
                for (JButton btn : buttons) btn.setFont(uiBoldFont);

                // Scale Border Panel Layout Widths Proportionally
                int requestedSidebarWidth = Math.max(260, (int)(frameWidth * 0.28));
                controlPanel.setPreferredSize(new Dimension(requestedSidebarWidth, getHeight()));

                // Resize Titled Border Titles dynamically
                JPanel[] panels = {studentPanel, adminPanel, adminRemovePanel};
                for (JPanel p : panels) 
                {
                    if (p.getBorder() instanceof TitledBorder) 
                    {
                        ((TitledBorder) p.getBorder()).setTitleFont(new Font("Segoe UI", Font.BOLD, (int)(dynamicFont * 0.95)));
                    }
                }
                controlPanel.revalidate();
                adminExitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
            }
        });

        // --- 5. System Transaction Logic Action Listeners ---
        borrowButton.addActionListener(e -> {
            String inputBookId = bookIdField.getText().trim();
            if (inputBookId.isEmpty()) 
            { 
                JOptionPane.showMessageDialog(this, "Please Enter a Book ID."); 
                return; 
            }

            libraryService.borrowBook(currentUserId, inputBookId);
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

        addBookButton.addActionListener(e -> {
            String inputBookId = newBookIdField.getText().trim();
            String inputTitle = newTitleField.getText().trim();
            String inputAuthor = newAuthorField.getText().trim();

            if (inputBookId.isEmpty() || inputTitle.isEmpty() || inputAuthor.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please complete all fields to catalog a new entry.", "System Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            libraryService.addBook(new Book(inputBookId, inputTitle, inputAuthor));
            libraryService.savedBooks();
            updateTableDisplay();

            newBookIdField.setText(""); newTitleField.setText(""); newAuthorField.setText("");
            JOptionPane.showMessageDialog(this, "Record saved successfully.", "Notification", JOptionPane.INFORMATION_MESSAGE);
        });

        removeBookButton.addActionListener(e -> {
            String inputBookId = removeBookField.getText().trim();
            if (inputBookId.isEmpty()) 
            { 
                JOptionPane.showMessageDialog(this, "Please specify target ID.", "Notice", JOptionPane.ERROR_MESSAGE); 
                return; 
            }

            libraryService.removeBook(inputBookId);
            updateTableDisplay();
            removeBookField.setText("");
            JOptionPane.showMessageDialog(this, "Record purged successfully.", "Notification", JOptionPane.INFORMATION_MESSAGE);
        });

        studentExitButton.addActionListener(e -> { this.login.setVisible(true); this.dispose(); });
        adminExitButton.addActionListener(e -> { this.login.setVisible(true); this.dispose(); });

        User logInUser = null;
        for (User u : libraryService.getUserList()) 
        {
            if (u.getUserId().equals(currentUserId)) { logInUser = u; break; }
        }
        configureRoleBasedAccess(logInUser);
    }

    public void updateTableDisplay() 
    {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        for (Book b : libraryService.getBookList()) {
            Object[] rowData = { b.getId(), b.getTitle(), b.getAuthor(), b.isAvailable() ? "Available" : "Borrowed" };
            model.addRow(rowData);
        }
    }

    public void configureRoleBasedAccess(User user) 
    {
        if (user == null) return;
        boolean isStudent = (user instanceof Student);

        adminPanel.setVisible(!isStudent);
        adminRemovePanel.setVisible(!isStudent);
        adminExitButton.setVisible(!isStudent);

        studentPanel.setVisible(isStudent);

        if (isStudent) 
        {
            Student s = (Student) user;
            welcomeLabel.setText("Active Profile: " + s.getName() + " (Student)  ||  Checked Out Records: " + s.getBorrowedCount() + " / 3 Limit");
        } 
        else 
        {
            Librarian l = (Librarian) user;
            welcomeLabel.setText("Active Profile: " + l.getName() + " (Librarian Operations Desk)");
        }
    }
}