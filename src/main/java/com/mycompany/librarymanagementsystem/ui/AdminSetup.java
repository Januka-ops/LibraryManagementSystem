
package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.AdminDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminSetup extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);

    public AdminSetup() {

        setTitle("Library Management System - Admin Setup");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(35, 45, 35, 45));

        JLabel title = new JLabel("Create Administrator");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(NAVY);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Set up your first administrator account");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(BG);
        heading.add(title);
        heading.add(subtitle);

        JPanel form = new JPanel(new GridLayout(6, 1, 5, 8));
        form.setBackground(BG);
        form.setBorder(new EmptyBorder(35, 0, 20, 0));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        form.add(createFieldPanel("Username", txtUsername));
        form.add(createFieldPanel("Password", txtPassword));
        form.add(createFieldPanel("Confirm Password", txtConfirmPassword));

        JButton btnCreate = new JButton("Create Admin Account");
        btnCreate.setBackground(BLUE);
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFont(new Font("Arial", Font.BOLD, 15));
        btnCreate.setFocusPainted(false);
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Arial", Font.PLAIN, 14));
        btnClear.setFocusPainted(false);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setBackground(BG);
        buttons.add(btnCreate);
        buttons.add(btnClear);

        JPanel bottom = new JPanel(new BorderLayout(0, 12));
        bottom.setBackground(BG);
        bottom.add(buttons, BorderLayout.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(form, BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);

        btnCreate.addActionListener(e -> createAdmin());
        btnClear.addActionListener(e -> clearFields());
    }

    private JPanel createFieldPanel(String label, JComponent field) {

        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BG);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(NAVY);

        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(100, 38));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void createAdmin() {

        String username = txtUsername.getText().trim();

        String password = new String(txtPassword.getPassword());
        String confirmPassword =
                new String(txtConfirmPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all fields."
            );
            return;
        }

        if (username.length() < 4) {
            JOptionPane.showMessageDialog(
                    this, "Username must contain at least 4 characters."
            );
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(
                    this, "Password must contain at least 8 characters."
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this, "Passwords do not match."
            );
            return;
        }

        try {

            AdminDAO dao = new AdminDAO();

            if (dao.hasAdmin()) {
                JOptionPane.showMessageDialog(
                        this,
                        "An administrator account already exists."
                );
                return;
            }

            if (dao.registerAdmin(username, password)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Administrator account created successfully!"
                );

                dispose();

                new Login().setVisible(true);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not create administrator: " + ex.getMessage()
            );
        }
    }

    private void clearFields() {

        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
