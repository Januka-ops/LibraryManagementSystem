package com.mycompany.librarymanagementsystem.dao;

import com.mycompany.librarymanagementsystem.database.DBConnection;
import com.mycompany.librarymanagementsystem.model.LibrarySettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDAO {

    // Retrieve library settings
    public LibrarySettings getSettings() throws SQLException {

        String sql = "SELECT * FROM library_settings WHERE setting_id = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                LibrarySettings settings = new LibrarySettings();

                settings.setLibraryName(rs.getString("library_name"));
                settings.setContactEmail(rs.getString("contact_email"));
                settings.setContactPhone(rs.getString("contact_phone"));
                settings.setBorrowingPeriodDays(
                        rs.getInt("borrowing_period_days"));

                return settings;
            }
        }

        throw new SQLException("Library settings record not found.");
    }

    // Update library settings
    public void updateSettings(LibrarySettings settings)
            throws SQLException {

        String sql = "UPDATE library_settings SET "
                + "library_name = ?, "
                + "contact_email = ?, "
                + "contact_phone = ?, "
                + "borrowing_period_days = ? "
                + "WHERE setting_id = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, settings.getLibraryName());
            ps.setString(2, settings.getContactEmail());
            ps.setString(3, settings.getContactPhone());
            ps.setInt(4, settings.getBorrowingPeriodDays());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Could not update library settings.");
            }
        }
    }
}