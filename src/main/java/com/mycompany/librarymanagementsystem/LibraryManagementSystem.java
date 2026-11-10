
package com.mycompany.librarymanagementsystem;

import com.mycompany.librarymanagementsystem.dao.AdminDAO;
import com.mycompany.librarymanagementsystem.ui.AdminSetup;
import com.mycompany.librarymanagementsystem.ui.Login;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class LibraryManagementSystem {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                AdminDAO adminDAO = new AdminDAO();

                // Check whether an admin account exists
                if (adminDAO.hasAdmin()) {

                    // Open Login screen
                    new Login().setVisible(true);

                } else {

                    // Open first-time Admin Setup
                    new AdminSetup().setVisible(true);
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Unable to connect to the database.\n"
                        + ex.getMessage(),
                        "Database Connection Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
