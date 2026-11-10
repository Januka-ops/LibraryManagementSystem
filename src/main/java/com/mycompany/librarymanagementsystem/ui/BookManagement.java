/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.BookDAO;
import com.mycompany.librarymanagementsystem.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookManagement extends JPanel {

    // Dashboard colour theme
    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);
    private final Color TEXT = new Color(20, 48, 91);
    private final Color LIGHT_BLUE = new Color(225, 239, 255);
    private final Color BORDER = new Color(220, 232, 248);

    // Form fields
    private JTextField txtTitle, txtAuthor, txtCategory;
    private JTextField txtISBN, txtQuantity, txtAvailable;
    private JTextField txtSearch;

    // Table
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // DAO
    private BookDAO bookDAO;

    // Selected book ID
    private int selectedBookId = -1;

    public BookManagement() {

        bookDAO = new BookDAO();

        setBackground(BG);
        setLayout(new BorderLayout());

        // Display Book Management content only.
        add(createMainContent(), BorderLayout.CENTER);

        // Load books from MySQL.
        loadBooks();
    }

    // ================= MAIN CONTENT =================
    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(0, 22));

        main.setBackground(BG);
        main.setBorder(new EmptyBorder(22, 25, 25, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);

        JLabel pageLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");

        pageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        pageLabel.setForeground(new Color(75, 112, 165));

        JLabel admin = new JLabel("🔔    Admin");

        admin.setFont(new Font("SansSerif", Font.BOLD, 14));
        admin.setForeground(TEXT);

        header.add(pageLabel, BorderLayout.WEST);
        header.add(admin, BorderLayout.EAST);

        // Welcome section
        JPanel welcome = new JPanel();
        welcome.setOpaque(false);
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Book Management");

        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Add, manage, update and search your library books."
        );

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(new Color(75, 112, 165));

        welcome.add(title);
        welcome.add(Box.createVerticalStrut(8));
        welcome.add(subtitle);

        // Top section
        JPanel top = new JPanel();

        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(header);
        top.add(Box.createVerticalStrut(30));
        top.add(welcome);

        // Book form panel
        JPanel formPanel = createBookForm();

        // Table panel
        JPanel tablePanel = createTablePanel();

        // Arrange form and table vertically
        JPanel content = new JPanel(new BorderLayout(0, 20));

        content.setOpaque(false);

        content.add(formPanel, BorderLayout.NORTH);
        content.add(tablePanel, BorderLayout.CENTER);

        main.add(top, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);

        return main;
    }

    // ================= BOOK FORM =================
    private JPanel createBookForm() {

        JPanel panel = createWhitePanel("📖   Book Details");

        JPanel fields = new JPanel(new GridLayout(2, 3, 18, 15));

        fields.setOpaque(false);

        txtTitle = createTextField();
        txtAuthor = createTextField();
        txtCategory = createTextField();
        txtISBN = createTextField();
        txtQuantity = createTextField();
        txtAvailable = createTextField();

        addFormField(fields, "Book Title", txtTitle);
        addFormField(fields, "Author", txtAuthor);
        addFormField(fields, "Category", txtCategory);
        addFormField(fields, "ISBN", txtISBN);
        addFormField(fields, "Total Quantity", txtQuantity);
        addFormField(fields, "Available Quantity", txtAvailable);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 5)
        );

        buttonPanel.setOpaque(false);

        JButton btnAdd = createButton("Add Book", BLUE);
        JButton btnUpdate = createButton(
                "Update", new Color(16, 165, 117)
        );
        JButton btnDelete = createButton(
                "Delete", new Color(235, 67, 83)
        );
        JButton btnClear = createButton(
                "Clear", new Color(100, 116, 139)
        );

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearFields());

        panel.add(fields, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= TABLE PANEL =================
    private JPanel createTablePanel() {

        JPanel panel = createWhitePanel("📚   All Books");

        // Search section
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));

        searchPanel.setOpaque(false);

        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(300, 40));

        JButton btnSearch = createButton("Search", BLUE);

        JButton btnRefresh = createButton(
                "Show All", new Color(100, 116, 139)
        );

        JPanel searchButtons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        searchButtons.setOpaque(false);

        searchButtons.add(btnSearch);
        searchButtons.add(btnRefresh);

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        btnSearch.addActionListener(e -> searchBooks());

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadBooks();
        });

        // Table columns
        String[] columns = {
            "Book ID",
            "Title",
            "Author",
            "Category",
            "ISBN",
            "Quantity",
            "Available"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);

        bookTable.setRowHeight(40);
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bookTable.setForeground(TEXT);
        bookTable.setGridColor(new Color(230, 238, 248));
        bookTable.setSelectionBackground(LIGHT_BLUE);
        bookTable.setSelectionForeground(TEXT);
        bookTable.setShowVerticalLines(false);
        bookTable.setFillsViewportHeight(true);

        // Table heading
        bookTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        bookTable.getTableHeader().setBackground(
                new Color(235, 243, 253)
        );

        bookTable.getTableHeader().setForeground(TEXT);
        bookTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );

        bookTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(bookTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scrollPane.getViewport().setBackground(Color.WHITE);

        // Select a row to edit
        bookTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && bookTable.getSelectedRow() != -1) {

                int row = bookTable.getSelectedRow();

                selectedBookId = Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString()
                );

                txtTitle.setText(
                        tableModel.getValueAt(row, 1).toString()
                );

                txtAuthor.setText(
                        tableModel.getValueAt(row, 2).toString()
                );

                txtCategory.setText(
                        tableModel.getValueAt(row, 3).toString()
                );

                Object isbn = tableModel.getValueAt(row, 4);

                txtISBN.setText(isbn == null ? "" : isbn.toString());

                txtQuantity.setText(
                        tableModel.getValueAt(row, 5).toString()
                );

                txtAvailable.setText(
                        tableModel.getValueAt(row, 6).toString()
                );
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= REUSABLE DESIGN METHODS =================
    private JPanel createWhitePanel(String title) {

        JPanel panel = new JPanel(new BorderLayout(0, 18));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(18, 18, 18, 18)
                )
        );

        JLabel heading = new JLabel(title);

        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setForeground(TEXT);

        panel.add(heading, BorderLayout.NORTH);

        return panel;
    }

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setPreferredSize(new Dimension(180, 38));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(7, 10, 7, 10)
                )
        );

        return field;
    }

    private void addFormField(
            JPanel panel,
            String labelText,
            JTextField field
    ) {

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 7));

        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);

        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT);

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        panel.add(fieldPanel);
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(110, 40));

        return button;
    }

    // ================= DATABASE OPERATIONS =================
    private void addBook() {

        if (!validateFields()) {
            return;
        }

        Book book = getBookFromFields();

        if (bookDAO.addBook(book)) {

            JOptionPane.showMessageDialog(
                    this, "Book added successfully!"
            );

            clearFields();
            loadBooks();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Could not add book."
            );
        }
    }

    private void updateBook() {

        if (selectedBookId == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please select a book from the table."
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        Book book = getBookFromFields();
        book.setBookId(selectedBookId);

        if (bookDAO.updateBook(book)) {

            JOptionPane.showMessageDialog(
                    this, "Book updated successfully!"
            );

            clearFields();
            loadBooks();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Could not update book."
            );
        }
    }

    private void deleteBook() {

        if (selectedBookId == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please select a book from the table."
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (bookDAO.deleteBook(selectedBookId)) {

                JOptionPane.showMessageDialog(
                        this, "Book deleted successfully!"
                );

                clearFields();
                loadBooks();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Could not delete book."
                );
            }
        }
    }

    private void loadBooks() {

        tableModel.setRowCount(0);

        List<Book> books = bookDAO.getAllBooks();

        displayBooks(books);
    }

    private void searchBooks() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }

        tableModel.setRowCount(0);

        List<Book> books = bookDAO.searchBooks(keyword);

        displayBooks(books);
    }

    private void displayBooks(List<Book> books) {

        for (Book book : books) {

            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getIsbn(),
                book.getQuantity(),
                book.getAvailableQuantity()
            });
        }
    }

    // ================= VALIDATION =================
    private boolean validateFields() {

        if (txtTitle.getText().trim().isEmpty()
                || txtAuthor.getText().trim().isEmpty()
                || txtQuantity.getText().trim().isEmpty()
                || txtAvailable.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all required fields."
            );

            return false;
        }

        try {

            int quantity = Integer.parseInt(
                    txtQuantity.getText().trim()
            );

            int available = Integer.parseInt(
                    txtAvailable.getText().trim()
            );

            if (quantity <= 0 || available < 0
                    || available > quantity) {

                JOptionPane.showMessageDialog(
                        this,
                        "Available quantity must be between 0 and total quantity."
                );

                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Quantity must contain valid numbers."
            );

            return false;
        }

        return true;
    }

    // ================= HELPER METHODS =================
    private Book getBookFromFields() {

        Book book = new Book();

        book.setTitle(txtTitle.getText().trim());
        book.setAuthor(txtAuthor.getText().trim());
        book.setCategory(txtCategory.getText().trim());
        book.setIsbn(txtISBN.getText().trim());

        book.setQuantity(
                Integer.parseInt(txtQuantity.getText().trim())
        );

        book.setAvailableQuantity(
                Integer.parseInt(txtAvailable.getText().trim())
        );

        return book;
    }

    private void clearFields() {

        txtTitle.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
        txtISBN.setText("");
        txtQuantity.setText("");
        txtAvailable.setText("");

        selectedBookId = -1;

        bookTable.clearSelection();
    }
}
