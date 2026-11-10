package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.ReprotDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ReportsManagement extends JPanel {

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);
    private final Color TEXT = new Color(20, 48, 91);

    private ReprotDAO reportDAO = new ReprotDAO();

    private JLabel[] statValues = new JLabel[5];

    private DefaultTableModel popularModel;
    private DefaultTableModel overdueModel;
    private DefaultTableModel historyModel;

    private JTextField txtMemberSearch;

    public ReportsManagement() {

        setLayout(new BorderLayout(15, 20));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createReportTabs(), BorderLayout.CENTER);

        refreshReports();
    }

    // Heading and summary cards
    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBackground(BG);

        JLabel heading = new JLabel("Library Reports");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(NAVY);

        JButton btnRefresh = new JButton("Refresh Reports");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(BLUE);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setPreferredSize(new Dimension(150, 38));

        btnRefresh.addActionListener(e -> refreshReports());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BG);
        titlePanel.add(heading, BorderLayout.WEST);
        titlePanel.add(btnRefresh, BorderLayout.EAST);

        JPanel cards = new JPanel(new GridLayout(1, 5, 12, 0));
        cards.setBackground(BG);

        String[] titles = {
            "Total Books",
            "Members",
            "Issued Books",
            "Returned Books",
            "Overdue Books"
        };

        for (int i = 0; i < titles.length; i++) {
            cards.add(createStatCard(titles[i], i));
        }

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);

        return panel;
    }

    // Create one statistics card
    private JPanel createStatCard(String title, int index) {

        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 230, 245)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT);

        statValues[index] = new JLabel("0");
        statValues[index].setFont(
                new Font("Segoe UI", Font.BOLD, 27));
        statValues[index].setForeground(BLUE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(statValues[index], BorderLayout.CENTER);

        return card;
    }

    // Create report tabs
    private JTabbedPane createReportTabs() {

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setForeground(NAVY);

        tabs.addTab("Most Borrowed Books", createPopularBooksPanel());
        tabs.addTab("Overdue Books", createOverdueBooksPanel());
        tabs.addTab("Member History", createMemberHistoryPanel());

        return tabs;
    }

    // Most borrowed books table
    private JPanel createPopularBooksPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {
            "Book ID", "Book Title", "Author", "Times Borrowed"
        };

        JTable table = createTable(columns);

        popularModel = (DefaultTableModel) table.getModel();

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // Overdue books table
    private JPanel createOverdueBooksPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {
            "Issue ID", "Book Title", "Member Name",
            "Issue Date", "Due Date"
        };

        JTable table = createTable(columns);

        overdueModel = (DefaultTableModel) table.getModel();

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // Member borrowing history
    private JPanel createMemberHistoryPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);

        txtMemberSearch = new JTextField(20);
        txtMemberSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMemberSearch.setPreferredSize(new Dimension(200, 35));

        JButton btnSearch = new JButton("Search");
        JButton btnShowAll = new JButton("Show All");

        styleButton(btnSearch);
        styleButton(btnShowAll);

        btnSearch.addActionListener(e ->
                loadMemberHistory(txtMemberSearch.getText().trim()));

        btnShowAll.addActionListener(e -> {
            txtMemberSearch.setText("");
            loadMemberHistory("");
        });

        searchPanel.add(new JLabel("Search Member:"));
        searchPanel.add(txtMemberSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        String[] columns = {
            "Issue ID", "Member", "Book",
            "Issue Date", "Due Date", "Return Date", "Status"
        };

        JTable table = createTable(columns);

        historyModel = (DefaultTableModel) table.getModel();

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // Common table design
    private JTable createTable(String[] columns) {

        DefaultTableModel model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13));

        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);

        return table;
    }

    // Refresh all reports
    private void refreshReports() {

        try {

            // Load summary statistics
            int[] summary = reportDAO.getSummary();

            for (int i = 0; i < summary.length; i++) {
                statValues[i].setText(String.valueOf(summary[i]));
            }

            // Load most borrowed books
            popularModel.setRowCount(0);

            List<Object[]> popularBooks =
                    reportDAO.getMostBorrowedBooks();

            for (Object[] row : popularBooks) {
                popularModel.addRow(row);
            }

            // Load overdue books
            loadOverdueBooks();

            // Load all member history
            loadMemberHistory("");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load reports: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadOverdueBooks() {

        overdueModel.setRowCount(0);

        try {

            List<Object[]> overdueBooks =
                    reportDAO.getOverdueBooks();

            for (Object[] row : overdueBooks) {
                overdueModel.addRow(row);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this, "Error loading overdue books: "
                    + e.getMessage());
        }
    }

    private void loadMemberHistory(String keyword) {

        historyModel.setRowCount(0);

        try {

            List<Object[]> history =
                    reportDAO.getMemberHistory(keyword);

            for (Object[] row : history) {
                historyModel.addRow(row);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this, "Error loading member history: "
                    + e.getMessage());
        }
    }

    private void styleButton(JButton button) {

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
    }
}