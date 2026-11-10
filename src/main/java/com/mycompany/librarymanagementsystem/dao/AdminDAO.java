package com.mycompany.librarymanagementsystem.dao;

import com.mycompany.librarymanagementsystem.database.DBConnection;
import com.mycompany.librarymanagementsystem.security.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    // Check whether an admin account already exists
    public boolean hasAdmin() throws SQLException {

        String sql = "SELECT COUNT(*) FROM admins";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    // Register a new admin
    public boolean registerAdmin(String username, String password)
            throws Exception {

        String sql = "INSERT INTO admins (username, password_hash) "
                + "VALUES (?, ?)";

        String hashedPassword = PasswordUtil.hashPassword(password);

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, hashedPassword);

            return pst.executeUpdate() > 0;
        }
    }

    // Authenticate admin login
    public boolean login(String username, String password)
            throws Exception {

        String sql = "SELECT password_hash FROM admins WHERE username = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    String storedHash = rs.getString("password_hash");

                    return PasswordUtil.verifyPassword(
                            password, storedHash
                    );
                }
            }
        }

        return false;
    }

    // Change admin password
    public boolean changePassword(
            String username,
            String currentPassword,
            String newPassword) throws Exception {

        String selectSQL
                = "SELECT password_hash FROM admins WHERE username = ?";

        try (Connection con = DBConnection.getConnection(); PreparedStatement selectPst
                = con.prepareStatement(selectSQL)) {

            selectPst.setString(1, username);

            try (ResultSet rs = selectPst.executeQuery()) {

                if (!rs.next()) {
                    return false;
                }

                String storedHash = rs.getString("password_hash");

                // Verify the current password
                if (!PasswordUtil.verifyPassword(
                        currentPassword, storedHash)) {

                    return false;
                }
            }

            // Generate a new secure password hash
            String newHash
                    = PasswordUtil.hashPassword(newPassword);

            String updateSQL
                    = "UPDATE admins SET password_hash = ? WHERE username = ?";

            try (PreparedStatement updatePst
                    = con.prepareStatement(updateSQL)) {

                updatePst.setString(1, newHash);
                updatePst.setString(2, username);

                return updatePst.executeUpdate() > 0;
            }
        }
    }

}
