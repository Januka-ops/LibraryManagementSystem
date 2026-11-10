package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.BookDAO;
import com.mycompany.librarymanagementsystem.dao.MemberDAO;
import com.mycompany.librarymanagementsystem.dao.BookIssueDAO;
import com.mycompany.librarymanagementsystem.model.Book;
import com.mycompany.librarymanagementsystem.model.Member;
import com.mycompany.librarymanagementsystem.model.BookIssue;
import com.mycompany.librarymanagementsystem.dao.SettingsDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class IssueReturnManagement extends JPanel {

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);
    private final Color TEXT = new Color(20, 48, 91);

    private JComboBox<Book> cmbBook;
    private JComboBox<Member> cmbMember;

    private JTextField txtIssueDate;
    private JTextField txtDueDate;
    private JTextField txtSearch;

    private JTable issueTable;
    private DefaultTableModel tableModel;

    private BookIssueDAO issueDAO = new BookIssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();

    public IssueReturnManagement() {

        setLayout(new BorderLayout(20, 20));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadDropdowns();
        loadIssues("");
        clearForm();
    }

    // Create heading and issue form
    private JPanel createTopPanel() {

        JPanel topPanel = new JPanel(new BorderLayout(10, 20));
        topPanel.setBackground(BG);

        JLabel heading = new JLabel("Issue / Return Books");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(NAVY);

        topPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 15, 12));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 245)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        cmbBook = new JComboBox<>();
        cmbMember = new JComboBox<>();

        txtIssueDate = new JTextField(LocalDate.now().toString());
        txtDueDate = new JTextField(LocalDate.now().plusDays(14).toString());

        styleCombo(cmbBook);
        styleCombo(cmbMember);
        styleField(txtIssueDate);
        styleField(txtDueDate);

        // Display book titles in dropdown
        cmbBook.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value instanceof Book) {
                    setText(((Book) value).getTitle());
                }

                return this;
            }
        });

        // Display member names in dropdown
        cmbMember.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value instanceof Member) {
                    setText(((Member) value).getFullName());
                }

                return this;
            }
        });

        formPanel.add(createLabel("Select Book"));
        formPanel.add(createLabel("Select Member"));
        formPanel.add(createLabel("Issue Date (yyyy-mm-dd)"));
        formPanel.add(createLabel("Due Date (yyyy-mm-dd)"));

        formPanel.add(cmbBook);
        formPanel.add(cmbMember);
        formPanel.add(txtIssueDate);
        formPanel.add(txtDueDate);

        JButton btnIssue = createButton("Issue Book", BLUE);
        JButton btnClear = createButton("Clear", NAVY);

        btnIssue.addActionListener(e -> issueBook());
        btnClear.addActionListener(e -> clearForm());

        formPanel.add(btnIssue);
        formPanel.add(btnClear);

        JLabel note = new JLabel("Default borrowing period: 14 days");
        note.setForeground(TEXT);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(note);

        JLabel dateNote = new JLabel("Dates must use yyyy-mm-dd format");
        dateNote.setForeground(Color.GRAY);
        dateNote.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(dateNote);

        topPanel.add(formPanel, BorderLayout.CENTER);

        return topPanel;
    }

    // Create issue records table
    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(BG);

        JLabel title = new JLabel("Book Issue Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(NAVY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BG);

        txtSearch = new JTextField(18);
        styleField(txtSearch);

        JButton btnSearch = createButton("Search", BLUE);
        JButton btnShowAll = createButton("Show All", NAVY);
        JButton btnReturn = createButton("Return Selected Book", BLUE);

        btnSearch.addActionListener(e -> loadIssues(txtSearch.getText().trim()));
        btnShowAll.addActionListener(e -> {
            txtSearch.setText("");
            loadIssues("");
        });
        btnReturn.addActionListener(e -> returnBook());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        searchPanel.add(btnReturn);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG);
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {
            "Issue ID",
            "Book",
            "Member",
            "Issue Date",
            "Due Date",
            "Return Date",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        issueTable = new JTable(tableModel);
        issueTable.setRowHeight(30);
        issueTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        issueTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13));
        issueTable.getTableHeader().setBackground(NAVY);
        issueTable.getTableHeader().setForeground(Color.WHITE);

        issueTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(issueTable);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 245)));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Load available books and registered members
    private void loadDropdowns() {

        cmbBook.removeAllItems();
        cmbMember.removeAllItems();

        try {

            List<Book> books = bookDAO.getAllBooks();

            for (Book book : books) {
                if (book.getAvailableQuantity() > 0) {
                    cmbBook.addItem(book);
                }
            }

            List<Member> members = memberDAO.getAllMembers();

            for (Member member : members) {
                cmbMember.addItem(member);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this, "Error loading dropdowns: " + e.getMessage());
        }
    }

    // Issue a book
    private void issueBook() {

        Book selectedBook = (Book) cmbBook.getSelectedItem();
        Member selectedMember = (Member) cmbMember.getSelectedItem();

        if (selectedBook == null || selectedMember == null) {
            JOptionPane.showMessageDialog(
                    this, "Please select an available book and a member.");
            return;
        }

        try {

            Date issueDate = Date.valueOf(txtIssueDate.getText().trim());
            Date dueDate = Date.valueOf(txtDueDate.getText().trim());

            if (dueDate.before(issueDate)) {
                JOptionPane.showMessageDialog(
                        this, "Due date cannot be before issue date.");
                return;
            }

            issueDAO.issueBook(
                    selectedBook.getBookId(),
                    selectedMember.getMemberId(),
                    issueDate,
                    dueDate
            );

            JOptionPane.showMessageDialog(
                    this, "Book issued successfully!");

            clearForm();
            loadDropdowns();
            loadIssues("");

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter valid dates using yyyy-mm-dd.");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this, "Issue failed: " + e.getMessage());
        }
    }

    // Return the selected book
    private void returnBook() {

        int selectedRow = issueTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an issue record first.");
            return;
        }

        int issueId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString());

        String status = tableModel.getValueAt(selectedRow, 6).toString();

        if (!"ISSUED".equals(status)) {
            JOptionPane.showMessageDialog(
                    this, "This book has already been returned.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to return this book?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            issueDAO.returnBook(issueId, Date.valueOf(LocalDate.now()));

            JOptionPane.showMessageDialog(
                    this, "Book returned successfully!");

            loadDropdowns();
            loadIssues(txtSearch.getText().trim());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this, "Return failed: " + e.getMessage());
        }
    }

    // Load issue records into JTable
    private void loadIssues(String keyword) {

        tableModel.setRowCount(0);

        try {

            List<BookIssue> issues = issueDAO.getAllIssues(keyword);

            for (BookIssue issue : issues) {

                String returnDate = issue.getReturnDate() == null
                        ? "Not Returned"
                        : issue.getReturnDate().toString();

                tableModel.addRow(new Object[]{
                    issue.getIssueId(),
                    issue.getBookTitle(),
                    issue.getMemberName(),
                    issue.getIssueDate(),
                    issue.getDueDate(),
                    returnDate,
                    issue.getStatus()
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this, "Error loading issue records: " + e.getMessage());
        }
    }

    // Clear the form
    private void clearForm() {

        txtIssueDate.setText(LocalDate.now().toString());

        txtDueDate.setText(
                LocalDate.now()
                        .plusDays(getDefaultBorrowingPeriod())
                        .toString()
        );
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);
        return label;
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));

        return button;
    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(150, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 235)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleCombo(JComboBox<?> combo) {

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(150, 32));
    }

    private int getDefaultBorrowingPeriod() {

        try {
            SettingsDAO settingsDAO = new SettingsDAO();

            return settingsDAO.getSettings()
                    .getBorrowingPeriodDays();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load borrowing period. Using 14 days."
            );

            return 14;
        }
    }
}
