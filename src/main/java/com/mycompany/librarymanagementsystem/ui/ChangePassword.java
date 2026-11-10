
package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.AdminDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChangePassword extends JFrame {

    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    private String username;

    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);

    public ChangePassword(String username) {

        this.username = username;

        setTitle("Change Password");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Change Password");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(NAVY);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Update your administrator password");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(BG);
        heading.add(title);
        heading.add(subtitle);

        txtCurrentPassword = new JPasswordField();
        txtNewPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        JPanel form = new JPanel(new GridLayout(3, 1, 5, 15));
        form.setBackground(BG);
        form.setBorder(new EmptyBorder(30, 0, 25, 0));

        form.add(createFieldPanel(
                "Current Password", txtCurrentPassword));

        form.add(createFieldPanel(
                "New Password", txtNewPassword));

        form.add(createFieldPanel(
                "Confirm New Password", txtConfirmPassword));

        JButton btnChange = new JButton("UPDATE PASSWORD");
        btnChange.setBackground(BLUE);
        btnChange.setForeground(Color.WHITE);
        btnChange.setFont(new Font("Arial", Font.BOLD, 14));
        btnChange.setFocusPainted(false);

        JButton btnClear = new JButton("CLEAR");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setFocusPainted(false);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));
        buttons.setBackground(BG);
        buttons.add(btnChange);
        buttons.add(btnClear);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BG);
        bottom.add(buttons, BorderLayout.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(form, BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);

        btnChange.addActionListener(e -> updatePassword());
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

    private void updatePassword() {

        String currentPassword =
                new String(txtCurrentPassword.getPassword());

        String newPassword =
                new String(txtNewPassword.getPassword());

        String confirmPassword =
                new String(txtConfirmPassword.getPassword());

        if (currentPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in all fields.");

            return;
        }

        if (newPassword.length() < 8) {

            JOptionPane.showMessageDialog(
                    this,
                    "New password must contain at least 8 characters.");

            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this, "New passwords do not match.");

            return;
        }

        if (currentPassword.equals(newPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "New password must be different from current password.");

            return;
        }

        try {

            AdminDAO dao = new AdminDAO();

            boolean updated = dao.changePassword(
                    username,
                    currentPassword,
                    newPassword
            );

            if (updated) {

                JOptionPane.showMessageDialog(
                        this, "Password changed successfully!");

                clearFields();
                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Current password is incorrect.",
                        "Password Error",
                        JOptionPane.ERROR_MESSAGE
                );

                txtCurrentPassword.setText("");
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not change password: " + ex.getMessage());
        }
    }

    private void clearFields() {

        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
