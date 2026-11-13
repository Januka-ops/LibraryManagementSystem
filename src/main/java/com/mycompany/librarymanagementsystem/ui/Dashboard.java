/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.librarymanagementsystem.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mycompany.librarymanagementsystem.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.librarymanagementsystem.ui.BookManagement;

/**
 *
 * @author ACER
 */
public class Dashboard extends JFrame {

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);
    private final Color TEXT = new Color(20, 48, 91);

    // Summary card value labels
    private JLabel totalBooksValue;
    private JLabel totalMembersValue;
    private JLabel issuedBooksValue;
    private JLabel overdueBooksValue;

    // Summary card description labels
    private JLabel availableBooksInfo;
    private JLabel activeMembersInfo;
    private JLabel issuedBooksInfo;
    private JLabel overdueBooksInfo;

    // Table model for displaying book records
    private DefaultTableModel booksTableModel;

    private JPanel pageContainer;
    private JButton activeNavButton;
    private JButton dashboardNavButton;
    private JButton bookNavButton;
    private JButton memberNavButton;
    private JButton issueNavButton;
    private JButton reportsNavButton;
    private JButton settingsNavButton;

    private String currentAdminUsername;

    public Dashboard(String username) {
        this.currentAdminUsername = username;

        setTitle("Library Management System");
        setSize(1500, 900);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);
        setLayout(new BorderLayout()); // Use BorderLayout to divide the window into regions.

        // Container for changing pages
        pageContainer = new JPanel(new BorderLayout());

        // Display Dashboard initially
        pageContainer.add(createMainContent(), BorderLayout.CENTER);

        // Add sidebar and page container
        add(createSidebar(), BorderLayout.WEST);
        add(pageContainer, BorderLayout.CENTER);

        loadDashboardData();
        loadRecentBooks();

    }

    // ---------- CREATE SIDEBAR ----------
    // Builds the dark navy-blue navigation panel.
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BorderLayout());

        // Navigation menu
        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(22, 12, 10, 12));

        JLabel logo = new JLabel("  📚  SMART LIBRARY ");

        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 19));
        logo.setBorder(new EmptyBorder(10, 8, 35, 0));

        menu.add(logo);

        String[] items = {
            "⌂    Dashboard",
            "▣    Books",
            "♟    Members",
            "⇄    Issue / Return",
            "▤    Reports",
            "⚙    Settings"
        };

        // Create navigation buttons
        for (int i = 0; i < items.length; i++) {

            JButton button = new JButton(items[i]);

            button.setFont(new Font("SansSerif", Font.PLAIN, 15));
            button.setForeground(Color.WHITE);
            button.setBackground(i == 0 ? BLUE : NAVY);

            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            button.setPreferredSize(new Dimension(220, 48));
            button.setBorder(new EmptyBorder(10, 14, 10, 8));

            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (i == 0) {
                dashboardNavButton = button;

                button.addActionListener(e -> {
                    showDashboard();
                    setActiveNavigationButton(button);
                });

            } else if (i == 1) {
                bookNavButton = button;

                button.addActionListener(e -> {
                    showBookManagement();
                    setActiveNavigationButton(button);
                });

            } else if (i == 2) {
                memberNavButton = button;

                button.addActionListener(e -> {
                    showMemberManagement();
                    setActiveNavigationButton(button);
                });

            } else if (i == 3) {
                issueNavButton= button;

                button.addActionListener(e -> {
                    showIssueReturn();
                    setActiveNavigationButton(button);
                });

            } else if (i == 4) {
                reportsNavButton = button;

                button.addActionListener(e -> {
                    showReports();
                    setActiveNavigationButton(button);
                });

            } else if (i == 5) {
                settingsNavButton = button;

                button.addActionListener(e -> {
                    showSettings();
                    setActiveNavigationButton(button);
                });
            }

            menu.add(button);
            menu.add(Box.createVerticalStrut(8));
        }

        sidebar.add(menu, BorderLayout.NORTH);

        // Sidebar footer panel
        JPanel footerPanel = new JPanel();

        footerPanel.setOpaque(false);
        footerPanel.setLayout(
                new BoxLayout(footerPanel, BoxLayout.Y_AXIS)
        );

        footerPanel.setBorder(
                new EmptyBorder(10, 12, 20, 12)
        );

        // Current administrator
        JLabel adminLabel = new JLabel(
                "Logged in: " + currentAdminUsername
        );

        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        adminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Change Password button
        JButton btnChangePassword = new JButton("Change Password");

        btnChangePassword.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setBackground(BLUE);
        btnChangePassword.setMaximumSize(new Dimension(220, 42));
        btnChangePassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.setBorderPainted(false);
        btnChangePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnChangePassword.addActionListener(e -> {
            new ChangePassword(currentAdminUsername).setVisible(true);
        });

        // Logout button
        JButton btnLogout = new JButton("Logout");

        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(190, 55, 55));
        btnLogout.setMaximumSize(new Dimension(220, 42));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                dispose();

                new Login().setVisible(true);
            }
        });

        // Footer message
        JLabel footer = new JLabel(
                "<html><center>"
                + "Read &nbsp; • &nbsp; Learn &nbsp; • &nbsp; Grow"
                + "<br><br>Good Books, Better Minds"
                + "</center></html>",
                SwingConstants.CENTER
        );

        footer.setForeground(new Color(190, 215, 245));
        footer.setFont(new Font("Serif", Font.ITALIC, 14));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add footer components
        footerPanel.add(adminLabel);
        footerPanel.add(Box.createVerticalStrut(15));
        footerPanel.add(btnChangePassword);
        footerPanel.add(Box.createVerticalStrut(10));
        footerPanel.add(btnLogout);
        footerPanel.add(Box.createVerticalStrut(20));
        footerPanel.add(footer);

        sidebar.add(footerPanel, BorderLayout.SOUTH);

        setActiveNavigationButton(dashboardNavButton);

        return sidebar;
    }

    // ---------- CREATE MAIN CONTENT ----------
    // Creates the header, welcome section, summary cards,
    // recent books, and quick actions.
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 18));

        main.setBackground(BG);

        main.setBorder(new EmptyBorder(20, 22, 22, 22));

        // ---------- TOP HEADER ----------
        JPanel header = new JPanel(new BorderLayout(15, 10));

        header.setOpaque(false);

        // Search field displayed at the top.
        JTextField search = new JTextField(" Search books, members...");

        search.setFont(new Font("SansSerif", Font.PLAIN, 15));
        search.setPreferredSize(new Dimension(400, 43));
        search.setForeground(new Color(100, 110, 125));
        search.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 228, 245)
                        ),
                        new EmptyBorder(8, 10, 8, 10)
                )
        );

        // Librarian account information.
        JLabel admin = new JLabel("🔔    Admin");

        admin.setForeground(TEXT);

        admin.setFont(new Font("SansSerif", Font.BOLD, 14));

        header.add(search, BorderLayout.CENTER);
        header.add(admin, BorderLayout.EAST);

        // ---------- WELCOME SECTION ----------
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setOpaque(false);

        JPanel welcomeText = new JPanel();
        welcomeText.setOpaque(false);
        welcomeText.setLayout(new BoxLayout(welcomeText, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome Back, Librarian!");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Manage your library efficiently and easily.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        subtitle.setForeground(new Color(75, 112, 165));

        welcomeText.add(title);
        welcomeText.add(Box.createVerticalStrut(8));
        welcomeText.add(subtitle);

        // ---------- SUMMARY CARDS ----------
        // Four cards are arranged horizontally
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setOpaque(false);

        stats.add(
                createStatCard(
                        "TOTAL BOOKS",
                        "0",
                        "Available: 0",
                        new Color(225, 239, 255),
                        BLUE,
                        1
                )
        );

        stats.add(
                createStatCard(
                        "TOTAL MEMBERS",
                        "0",
                        "Active: 0",
                        new Color(222, 248, 239),
                        new Color(16, 165, 117),
                        2
                )
        );

        stats.add(
                createStatCard(
                        "BOOKS ISSUED",
                        "0",
                        "Issued to members",
                        new Color(235, 230, 255),
                        new Color(125, 86, 230),
                        3
                )
        );

        stats.add(
                createStatCard(
                        "OVERDUE BOOKS",
                        "0",
                        "To be returned",
                        new Color(255, 230, 232),
                        new Color(235, 67, 83),
                        4
                )
        );

        // Group the header, welcome section, and cards.
        JPanel top = new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(top, BoxLayout.Y_AXIS)
        );

        top.add(header);
        top.add(Box.createVerticalStrut(28));
        top.add(welcome);
        top.add(Box.createVerticalStrut(25));
        top.add(stats);

        // ---------- LOWER DASHBOARD PANELS ----------
        JPanel recentBooks = createBooksPanel();
        JPanel quickActions = createQuickActionsPanel();

        // GridBagLayout allows columns with different widths.
        JPanel middle = new JPanel(new GridBagLayout());

        middle.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;

        // Fill the available horizontal and vertical space.
        c.fill = GridBagConstraints.BOTH;

        c.weighty = 1;

        // First column: Recent Books.
        c.gridx = 0;
        c.weightx = 1.35;
        c.insets = new Insets(0, 0, 0, 14);

        middle.add(recentBooks, c);

        // Second column: Recent Activity.
        c.gridx = 2;
        c.weightx = 0.8;
        c.insets = new Insets(0, 0, 0, 0);

        middle.add(quickActions, c);

        main.add(top, BorderLayout.NORTH);
        main.add(middle, BorderLayout.CENTER);

        return main;
    }

    // ---------- CREATE SUMMARY CARD ----------
    // Creates one reusable statistics card.
    private JPanel createStatCard(
            String heading,
            String number,
            String detail,
            Color background,
            Color accent,
            int cardType
    ) {
        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(background);

        // Add a subtle border and internal spacing.
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        accent.getRed(),
                                        accent.getGreen(),
                                        accent.getBlue(),
                                        45
                                )
                        ),
                        new EmptyBorder(18, 18, 18, 12)
                )
        );

        // Card heading.
        JLabel label = new JLabel(heading);

        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Main statistic value.
        JLabel value = new JLabel(number);

        value.setForeground(accent.darker());
        value.setFont(new Font("SansSerif", Font.BOLD, 32));

        // Additional card information.
        JLabel info = new JLabel(detail);

        info.setForeground(accent.darker());
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Store the labels in class-level variables.
        // This allows other methods to update them.
        switch (cardType) {
            case 1:
                totalBooksValue = value;
                availableBooksInfo = info;
                break;

            case 2:
                totalMembersValue = value;
                activeMembersInfo = info;
                break;

            case 3:
                issuedBooksValue = value;
                issuedBooksInfo = info;
                break;

            case 4:
                overdueBooksValue = value;
                overdueBooksInfo = info;
                break;
        }

        // Arrange the labels vertically.
        card.add(label);
        card.add(Box.createVerticalStrut(10));
        card.add(value);
        card.add(Box.createVerticalStrut(8));
        card.add(info);

        return card;
    }

    // ---------- CREATE RECENT BOOKS TABLE ----------
    private JPanel createBooksPanel() {
        JPanel panel = createPanel("📚   Recent Books");

        String[] columns = {
            "ID",
            "Title",
            "Author",
            "Category",
            "Status"
        };

        // Temporary sample data.
//        Object[][] data = {
//            {
//                "B001",
//                "Clean Code",
//                "Robert C. Martin",
//                "Programming",
//                "Available"
//            },
//            {
//                "B002",
//                "Java for Beginners",
//                "Herbert Schildt",
//                "Programming",
//                "Issued"
//            },
//            {
//                "B003",
//                "Database Systems",
//                "Abraham Silberschatz",
//                "IT",
//                "Available"
//            },
//            {
//                "B004",
//                "The Great Gatsby",
//                "F. Scott Fitzgerald",
//                "Fiction",
//                "Available"
//            },
//            {
//                "B005",
//                "Atomic Habits",
//                "James Clear",
//                "Self Help",
//                "Overdue"
//            }
//        };
        // DefaultTableModel manages the rows and columns.
        // The overridden method prevents direct cell editing.
        booksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        JTable table = new JTable(booksTableModel);

        // Increase the height of each row.
        table.setRowHeight(43);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setGridColor(new Color(230, 238, 248));
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setShowVerticalLines(false);

        // Style the column heading area.
        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        table.getTableHeader().setBackground(
                new Color(235, 243, 253)
        );

        table.getTableHeader().setForeground(TEXT);

        table.getTableHeader().setPreferredSize(new Dimension(0, 38));

        // JScrollPane adds scrolling if needed.
        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ---------- CREATE QUICK ACTIONS ----------
    private JPanel createQuickActionsPanel() {

        JPanel panel = createPanel(
                "⚡   Quick Actions"
        );

        JPanel actions = new JPanel();

        actions.setBackground(Color.WHITE);

        actions.setBorder(
                new EmptyBorder(14, 12, 14, 12)
        );

        actions.setLayout(
                new BoxLayout(actions, BoxLayout.Y_AXIS)
        );

        // Labels displayed on the action buttons.
        String[] names = {
            "Add New Book",
            "Register Member",
            "Issue Book",
            "Return Book",
            "View Reports"
        };

        // Each button has its own accent color.
        Color[] colors = {
            new Color(35, 125, 235),
            new Color(23, 169, 121),
            new Color(125, 86, 230),
            new Color(231, 151, 45),
            new Color(20, 155, 190)
        };

        for (int i = 0; i < names.length; i++) {

            JButton button = new JButton(names[i]);

            button.setFont(
                    new Font("SansSerif", Font.BOLD, 12)
            );

            button.setForeground(Color.WHITE);

            button.setBackground(colors[i]);

            button.setFocusPainted(false);
            button.setBorderPainted(false);

            button.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 48)
            );

            button.setPreferredSize(
                    new Dimension(190, 48)
            );

            button.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            button.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            // Placeholder action.
            // We will open the real forms in later steps.
            switch (i) {
                case 0:
                    button.addActionListener(e -> {
                        showBookManagement();
                        setActiveNavigationButton(bookNavButton);
                    });
                    break;

                case 1:
                    button.addActionListener(e -> {
                        showMemberManagement();
                        setActiveNavigationButton(memberNavButton);
                    });
                    break;

                case 2:
                    button.addActionListener(e -> {
                        showIssueReturn();
                        setActiveNavigationButton(issueNavButton);
                    });
                    break;

                case 3:
                    button.addActionListener(e -> {
                        showIssueReturn();
                        setActiveNavigationButton(issueNavButton);
                    });
                    break;

                case 4:
                    button.addActionListener(e -> {
                        showReports();
                        setActiveNavigationButton(reportsNavButton);
                    });
                    break;
            }

            actions.add(button);

            actions.add(
                    Box.createVerticalStrut(13)
            );
        }

        panel.add(
                actions,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ---------- CREATE REUSABLE WHITE PANEL ----------
    // Used by Recent Books, Recent Activity, and Quick Actions.
    private JPanel createPanel(String title) {

        JPanel panel = new JPanel(
                new BorderLayout(0, 10)
        );

        panel.setBackground(Color.WHITE);

        // Add a light border and internal spacing.
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 232, 248)
                        ),
                        new EmptyBorder(14, 14, 14, 14)
                )
        );

        JLabel heading = new JLabel(title);

        heading.setForeground(TEXT);

        heading.setFont(
                new Font("SansSerif", Font.BOLD, 16)
        );

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        return panel;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(
//                        UIManager.getSystemLookAndFeelClassName()
//                );
//            } catch (Exception ignored) {
//            }
//
//            new Dashboard().setVisible(true);
//        });
//    }
    private void loadDashboardData() {
        // SQL query to retrieve all four statistics.
        String query
                = "SELECT "
                + "(SELECT COALESCE(SUM(quantity), 0) FROM books) AS total_books, "
                + "(SELECT COUNT(*) FROM members) AS total_members, "
                + "(SELECT COUNT(*) FROM book_issues "
                + "WHERE return_date IS NULL AND status = 'ISSUED') AS issued_books, "
                + "(SELECT COUNT(*) FROM book_issues "
                + "WHERE return_date IS NULL "
                + "AND due_date < CURDATE() "
                + "AND status = 'ISSUED') AS overdue_books";

        // Establish the database connection.
        try (Connection con = DBConnection.getConnection(); // Prepare the SQL query.
                 PreparedStatement pst = con.prepareStatement(query); // Execute the query and retrieve results.
                 ResultSet rs = pst.executeQuery()) {

            // Move to the first result row.
            if (rs.next()) {
                // Retrieve values using column aliases.
                int totalBooks = rs.getInt("total_books");
                int totalMembers = rs.getInt("total_members");
                int issuedBooks = rs.getInt("issued_books");
                int overdueBooks = rs.getInt("overdue_books");

                // Update dashboard summary cards.
                totalBooksValue.setText(String.valueOf(totalBooks));
                totalMembersValue.setText(String.valueOf(totalMembers));
                issuedBooksValue.setText(String.valueOf(issuedBooks));
                overdueBooksValue.setText(String.valueOf(overdueBooks));

                // Update additional information.
                int availableBooks = getAvailableBooks();

                availableBooksInfo.setText(
                        "Available: " + availableBooks
                );

                activeMembersInfo.setText(
                        "Active: " + totalMembers
                );

                issuedBooksInfo.setText(
                        "Currently issued"
                );

                overdueBooksInfo.setText(
                        "To be returned"
                );
            }

        } catch (SQLException e) {
            // Display an error if database retrieval fails.
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load dashboard data:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Retrieves the total number of available book copies.
    private int getAvailableBooks() {

        String query
                = "SELECT COALESCE(SUM(available_quantity), 0) "
                + "FROM books";

        try (
                Connection con = DBConnection.getConnection(); PreparedStatement pst
                = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void loadRecentBooks() {

        // SQL query to retrieve book details.
        String query
                = "SELECT b.book_id, b.title, b.author, b.category, "
                + "CASE "
                + // Check whether the book has an overdue issue.
                "WHEN EXISTS ("
                + "SELECT 1 FROM book_issues bi "
                + "WHERE bi.book_id = b.book_id "
                + "AND bi.return_date IS NULL "
                + "AND bi.due_date < CURDATE()"
                + ") THEN 'Overdue' "
                + // Otherwise check whether copies are available.
                "WHEN b.available_quantity > 0 THEN 'Available' "
                + "ELSE 'Issued' "
                + "END AS book_status "
                + "FROM books b "
                + "ORDER BY b.book_id DESC "
                + "LIMIT 5";

        try (
                Connection con = DBConnection.getConnection(); PreparedStatement pst
                = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            // Clear old records before loading new ones.
            booksTableModel.setRowCount(0);

            // Read each database record.
            while (rs.next()) {

                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                String status = rs.getString("book_status");

                booksTableModel.addRow(new Object[]{
                    "B" + String.format("%03d", id),
                    title,
                    author,
                    category,
                    status
                });
            }

        } catch (SQLException e) {

            // Display an error if book retrieval fails.
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load books:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void setActiveNavigationButton(JButton selectedButton) {

        // Reset the previous button
        if (activeNavButton != null) {
            activeNavButton.setBackground(NAVY);
            activeNavButton.setForeground(Color.WHITE);
        }

        // Highlight the selected button
        activeNavButton = selectedButton;

        activeNavButton.setBackground(BLUE);
        activeNavButton.setForeground(Color.WHITE);
    }

    private void showBookManagement() {

        pageContainer.removeAll();

        pageContainer.add(
                new BookManagement(),
                BorderLayout.CENTER
        );

        pageContainer.revalidate();
        pageContainer.repaint();
    }

    private void showMemberManagement() {

        pageContainer.removeAll();

        pageContainer.add(
                new MemberManagement(),
                BorderLayout.CENTER
        );

        pageContainer.revalidate();
        pageContainer.repaint();
    }

    private void showIssueReturn() {

        pageContainer.removeAll();

        pageContainer.add(
                new IssueReturnManagement(),
                BorderLayout.CENTER
        );

        pageContainer.revalidate();
        pageContainer.repaint();
    }

    private void showReports() {

        pageContainer.removeAll();

        pageContainer.add(
                new ReportsManagement(),
                BorderLayout.CENTER
        );

        pageContainer.revalidate();
        pageContainer.repaint();
    }

    private void showSettings() {

        pageContainer.removeAll();

        pageContainer.add(
                new SettingManagement(),
                BorderLayout.CENTER
        );

        pageContainer.revalidate();
        pageContainer.repaint();
    }

    private void showDashboard() {

        pageContainer.removeAll();

        pageContainer.add(
                createMainContent(),
                BorderLayout.CENTER
        );

        loadDashboardData();
        loadRecentBooks();

        pageContainer.revalidate();
        pageContainer.repaint();
    }
}
