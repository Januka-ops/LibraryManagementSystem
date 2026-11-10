package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.AdminDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);

    public Login() {

        setTitle("Library Management System - Admin Login");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(35, 40, 35, 40));

        JLabel title = new JLabel("Library Management");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(NAVY);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Administrator Login");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(BG);
        heading.add(title);
        heading.add(subtitle);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JPanel form = new JPanel(new GridLayout(4, 1, 5, 12));
        form.setBackground(BG);
        form.setBorder(new EmptyBorder(40, 0, 25, 0));

        form.add(createFieldPanel("Username", txtUsername));
        form.add(createFieldPanel("Password", txtPassword));

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnClear = new JButton("CLEAR");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setFocusPainted(false);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setBackground(BG);
        buttons.add(btnLogin);
        buttons.add(btnClear);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BG);
        bottom.add(buttons, BorderLayout.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(form, BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);

        btnLogin.addActionListener(e -> loginAdmin());
        btnClear.addActionListener(e -> clearFields());

        txtPassword.addActionListener(e -> loginAdmin());
    }

    private JPanel createFieldPanel(String label, JComponent field) {

        JPanel panel = new JPanel(new BorderLayout(0, 7));
        panel.setBackground(BG);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(NAVY);

        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(100, 40));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void loginAdmin() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please enter your username and password."
            );

            return;
        }

        try {

            AdminDAO dao = new AdminDAO();

            if (dao.login(username, password)) {

                JOptionPane.showMessageDialog(
                        this, "Login successful! Welcome " + username
                );

                Dashboard dashboard = new Dashboard(username);
                dashboard.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                txtPassword.setText("");
                txtPassword.requestFocus();
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        txtUsername.setText("");
        txtPassword.setText("");
    }
}
