package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.SettingsDAO;
import com.mycompany.librarymanagementsystem.model.LibrarySettings;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class SettingManagement extends JPanel {

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);
    private final Color TEXT = new Color(20, 48, 91);

    private JTextField txtLibraryName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtBorrowingDays;

    private SettingsDAO settingsDAO = new SettingsDAO();

    public SettingManagement() {

        setLayout(new BorderLayout(20, 20));
        setBackground(BG);

        setBorder(BorderFactory.createEmptyBorder(
                30, 35, 30, 35));

        add(createHeader(), BorderLayout.NORTH);
        add(createSettingsPanel(), BorderLayout.CENTER);

        loadSettings();
    }

    // Page heading
    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

        JLabel heading = new JLabel("System Settings");

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 28));

        heading.setForeground(NAVY);

        JLabel description = new JLabel(
                "Manage your library information and borrowing preferences.");

        description.setFont(
                new Font("Segoe UI", Font.PLAIN, 14));

        description.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(BG);

        textPanel.add(heading);
        textPanel.add(description);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    // Main settings form
    private JPanel createSettingsPanel() {

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(BG);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 230, 245)),
                BorderFactory.createEmptyBorder(
                        30, 35, 30, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel sectionTitle = new JLabel("Library Information");

        sectionTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 20));

        sectionTitle.setForeground(NAVY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(sectionTitle, gbc);

        gbc.gridwidth = 1;

        // Library name
        addFormRow(formPanel, gbc, 1,
                "Library Name:", txtLibraryName = createField());

        // Email
        addFormRow(formPanel, gbc, 2,
                "Contact Email:", txtEmail = createField());

        // Phone
        addFormRow(formPanel, gbc, 3,
                "Contact Phone:", txtPhone = createField());

        // Borrowing period
        addFormRow(formPanel, gbc, 4,
                "Borrowing Period (Days):",
                txtBorrowingDays = createField());

        JLabel note = new JLabel(
                "Default borrowing period is 14 days.");

        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(Color.GRAY);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(note, gbc);

        // Buttons
        JButton btnSave = createButton("Save Changes", BLUE);
        JButton btnReset = createButton("Reset Defaults", NAVY);

        btnSave.addActionListener(e -> saveSettings());
        btnReset.addActionListener(e -> resetSettings());

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 15, 5));

        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnReset);

        gbc.gridx = 1;
        gbc.gridy = 6;

        formPanel.add(buttonPanel, gbc);

        outerPanel.add(formPanel, BorderLayout.NORTH);

        return outerPanel;
    }

    // Add label and text field
    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JTextField field) {

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 14));

        label.setForeground(TEXT);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        panel.add(field, gbc);
    }

    // Create text fields
    private JTextField createField() {

        JTextField field = new JTextField(25);

        field.setFont(
                new Font("Segoe UI", Font.PLAIN, 14));

        field.setPreferredSize(new Dimension(300, 38));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 220, 235)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        return field;
    }

    // Create buttons
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 13));

        button.setBackground(color);
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(150, 40));

        return button;
    }

    // Load settings from database
    private void loadSettings() {

        try {

            LibrarySettings settings =
                    settingsDAO.getSettings();

            txtLibraryName.setText(settings.getLibraryName());

            txtEmail.setText(settings.getContactEmail());

            txtPhone.setText(settings.getContactPhone());

            txtBorrowingDays.setText(
                    String.valueOf(settings.getBorrowingPeriodDays()));

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load settings: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Save settings
    private void saveSettings() {

        String libraryName = txtLibraryName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String daysText = txtBorrowingDays.getText().trim();

        if (libraryName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Library name cannot be empty.");

            return;
        }

        if (!email.isEmpty()
                && !email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid email address.");

            return;
        }

        int borrowingDays;

        try {

            borrowingDays = Integer.parseInt(daysText);

            if (borrowingDays < 1 || borrowingDays > 365) {

                JOptionPane.showMessageDialog(
                        this,
                        "Borrowing period must be between 1 and 365 days.");

                return;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid borrowing period.");

            return;
        }

        LibrarySettings settings = new LibrarySettings(
                libraryName,
                email,
                phone,
                borrowingDays
        );

        try {

            settingsDAO.updateSettings(settings);

            JOptionPane.showMessageDialog(
                    this, "Settings saved successfully!");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save settings: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Reset to default settings
    private void resetSettings() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to reset all settings?",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        txtLibraryName.setText("Library Management System");
        txtEmail.setText("");
        txtPhone.setText("");
        txtBorrowingDays.setText("14");

        saveSettings();
    }
}