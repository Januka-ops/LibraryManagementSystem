package com.mycompany.librarymanagementsystem.dao;

import com.mycompany.librarymanagementsystem.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReprotDAO {

    // Get summary statistics
    public int[] getSummary() throws SQLException {

        int[] summary = new int[5];

        String sql =
            "SELECT " +
            "(SELECT COALESCE(SUM(quantity), 0) FROM books), " +
            "(SELECT COUNT(*) FROM members), " +
            "(SELECT COUNT(*) FROM book_issues " +
            " WHERE status = 'ISSUED' AND return_date IS NULL), " +
            "(SELECT COUNT(*) FROM book_issues " +
            " WHERE status = 'RETURNED'), " +
            "(SELECT COUNT(*) FROM book_issues " +
            " WHERE status = 'ISSUED' AND return_date IS NULL " +
            " AND due_date < CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                for (int i = 0; i < summary.length; i++) {
                    summary[i] = rs.getInt(i + 1);
                }
            }
        }

        return summary;
    }

    // Get most borrowed books
    public List<Object[]> getMostBorrowedBooks() throws SQLException {

        List<Object[]> list = new ArrayList<>();

        String sql =
            "SELECT b.book_id, b.title, b.author, " +
            "COUNT(bi.issue_id) AS borrow_count " +
            "FROM books b " +
            "LEFT JOIN book_issues bi ON b.book_id = bi.book_id " +
            "GROUP BY b.book_id, b.title, b.author " +
            "ORDER BY borrow_count DESC, b.title ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("borrow_count")
                });
            }
        }

        return list;
    }

    // Get overdue books
    public List<Object[]> getOverdueBooks() throws SQLException {

        List<Object[]> list = new ArrayList<>();

        String sql =
            "SELECT bi.issue_id, b.title, m.full_name, " +
            "bi.issue_date, bi.due_date " +
            "FROM book_issues bi " +
            "JOIN books b ON bi.book_id = b.book_id " +
            "JOIN members m ON bi.member_id = m.member_id " +
            "WHERE bi.status = 'ISSUED' " +
            "AND bi.return_date IS NULL " +
            "AND bi.due_date < CURDATE() " +
            "ORDER BY bi.due_date ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("issue_id"),
                    rs.getString("title"),
                    rs.getString("full_name"),
                    rs.getDate("issue_date"),
                    rs.getDate("due_date")
                });
            }
        }

        return list;
    }

    // Get borrowing history for members
    public List<Object[]> getMemberHistory(String keyword)
            throws SQLException {

        List<Object[]> list = new ArrayList<>();

        String sql =
            "SELECT bi.issue_id, m.full_name, b.title, " +
            "bi.issue_date, bi.due_date, bi.return_date, bi.status " +
            "FROM book_issues bi " +
            "JOIN members m ON bi.member_id = m.member_id " +
            "JOIN books b ON bi.book_id = b.book_id " +
            "WHERE m.full_name LIKE ? OR m.email LIKE ? " +
            "ORDER BY bi.issue_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Date returnDate = rs.getDate("return_date");

                    list.add(new Object[]{
                        rs.getInt("issue_id"),
                        rs.getString("full_name"),
                        rs.getString("title"),
                        rs.getDate("issue_date"),
                        rs.getDate("due_date"),
                        returnDate == null ? "Not Returned" : returnDate,
                        rs.getString("status")
                    });
                }
            }
        }

        return list;
    }
}